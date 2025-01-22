package base;

import controllers.BookControl;
import controllers.CommunicationManager;
import controllers.DBControl;
import controllers.SubscriberControl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class handles managing jobs.
 * Jobs are tasks that repeat every some time.
 *
 * For example generating a report is done at the end of the month.
 */
public class JobManager {
    public JobManager() {
        // Runs every hour to run some task
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runJobs();
            }
        }, 0, 60 * 60 * 1000);
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                runOnceADayJobs();
            }
        }, 0, 24 * 60 * 60 * 1000);
    }

    /**
     * This method runs all jobs. It's called by a fixed-rate timer in the constructor
     */
    public void runJobs() {
        generateReports();
        checkForLateBorrows();
    }

    /**
     * This method runs jobs that need to be done once a day
     */
    public void runOnceADayJobs(){
        sendBookReturnReminders();
    }

    public void sendBookReturnReminders(){
        final String messageTemplate = "Hi %s,\nWe wanted to remind you that you have to return %s book to the library until tomorrow.\nBlib Library.";
        final String htmlMessageTemplate ="Hi %s,<br>We wanted to remind you that you have to return %s book to the library until tomorrow.<br>Blib Library.";

        ArrayList<Map<String, Object>> records = BookControl.getBooksForReturnReminder();
        for (Map<String, Object> record : records) {
            CommunicationManager.sendSMS((String)record.get("phone_number"), String.format(messageTemplate, record.get("name"), record.get("title")));
            CommunicationManager.sendMail((String) record.get("email"), String.format("Returning %s", record.get("title")),
                    String.format(htmlMessageTemplate, record.get("name"), record.get("title")), "Blib Reminders");
        }
    }

    /**
     * Tries and generates a report at the end of each month (or start of each month)
     */
    public void generateReports() {
        LocalDateTime date = getJobDate("generate-reports");
        LocalDate nowMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate now = LocalDate.now().withDayOfMonth(1);

        if (date == null || !date.getMonth().equals(now.getMonth())) {
            // The moment that the next month enters, the last month "ends".
            // We want to also ensure that the report is generated in case the app isn't on by the 1st of the month.
            generateSubscriberStatusReport(nowMonth);
            generateBorrowTimesReport(nowMonth);

            try {
                markJobDone("generate-reports");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Generates a report for subscribers status
     * It fetches the data from the subscriber history, taking any freeze that either start after the month
     * or hasn't ended in the month (Basically what is relevant for the chart of the month)
     * @param date
     */
    public void generateSubscriberStatusReport(LocalDate date) {
        String query = "SELECT * FROM subscriber_history " +
                "WHERE action = 'freeze' AND (date >= ? OR end_date <= ?)";
        try (PreparedStatement st = DBControl.prepareStatement(query)) {
            st.setObject(1, date);
            st.setObject(2, date);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String query2 = "INSERT INTO subscriber_status_report (subscriber_id, freeze_date, freeze_end_date, report_date) " +
                        "VALUES (?, ?, ?, ?)";

                try (PreparedStatement st2 = DBControl.prepareStatement(query2)) {
                    st2.setInt(1, rs.getInt("subscriber_id"));
                    st2.setDate(2, rs.getDate("date"));
                    st2.setDate(3, rs.getDate("end_date"));
                    st2.setObject(4, date);
                    st2.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateBorrowTimesReport(LocalDate date) {
        String query = "SELECT borrow.*, book_copy.book_id, late.date AS late_date, late.end_date AS late_return_date " +
                "FROM subscriber_history AS borrow " +
                "INNER JOIN book_copy ON book_copy_id = book_copy.id " +
                "LEFT JOIN subscriber_history AS late ON late.book_copy_id = borrow.book_copy_id AND late.action = 'late'" +
                "WHERE borrow.action = 'borrow' AND borrow.date >= ? AND borrow.date <= ? ";
        try (PreparedStatement st = DBControl.prepareStatement(query)) {
            st.setDate(1, Date.valueOf(date));
            st.setDate(2, Date.valueOf(date.plusMonths(1).minusDays(1)));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String query2 = "INSERT INTO borrow_report (book_id, book_copy_id, start_date, return_date, is_late, late_return_date, report_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement st2 = DBControl.prepareStatement(query2)) {
                    st2.setInt(1, rs.getInt("book_id"));
                    st2.setInt(2, rs.getInt("book_copy_id"));
                    st2.setTimestamp(3, rs.getTimestamp("date"));
                    st2.setTimestamp(4, rs.getTimestamp("end_date"));
                    if (rs.getTimestamp("late_date") != null) {
                        st2.setBoolean(5, true);
                        st2.setObject(6, rs.getDate("late_return_date"));
                    } else {
                        st2.setBoolean(5, false);
                        st2.setObject(6, null);
                    }
                    st2.setObject(7, date);

                    st2.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks for any late return. If it detects a week late return it punishes the offending subscriber
     * by freezing their account for a month (30 days)
     */
    public void checkForLateBorrows() {
        LocalDateTime date = getJobDate("check-borrows");
        LocalDateTime now = LocalDateTime.now();

        if (date == null || ChronoUnit.HOURS.between(date, now) >= 1) {
            String query = "SELECT book_copy.*, s.* FROM book_copy " +
                    "LEFT JOIN subscriber AS s ON book_copy.borrow_subscriber_id = s.id " +
                    "WHERE DATE_ADD(book_copy.return_date, INTERVAL 1 WEEK) < NOW()" +
                    "AND (s.frozen_until IS NULL OR s.frozen_until < NOW())";

            //TODO: add history entry for being late and then punish after a week

            try (Statement st = DBControl.createStatement()) {
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    SubscriberControl.freezeSubscriber(rs.getInt("borrow_subscriber_id"));
                }
                markJobDone("check-borrows");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Returns the last time the job got executed. Null if it never was.
     * @param jobName
     * @return Date
     * @throws SQLException
     */
    public LocalDateTime getJobDate(String jobName) {
        try (PreparedStatement ps = DBControl.getInstance().selectQuery("job","name", jobName)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("date").toLocalDateTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void markJobDone(String jobName) throws SQLException {
        PreparedStatement st;
        if (getJobDate(jobName) == null) {
            st = DBControl.prepareStatement("INSERT INTO job (date, name) VALUES (now(), ?)");
        } else {
            st = DBControl.prepareStatement("UPDATE job SET date = now() WHERE name = ?");
        }
        st.setString(1, jobName);
        st.execute();
        st.close();
    }
}
