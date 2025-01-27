package base;

import controllers.BookControl;
import controllers.CommunicationManager;
import controllers.DBControl;
import controllers.SubscriberControl;
import entities.HistoryAction;
import entities.HistoryEntry;

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
 * We store the last time they got activated in the DB to know whether or not they need to be ran again.
 * For example generating a report is done at the end of the month.
 * <br>
 * This idea is a reuse from an existing framework, Laravel.
 */
public class JobManager {
    public JobManager() {
        // Runs every hour to run some task
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                try {
                    runJobs(false);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 60 * 60 * 1000);
    }

    /**
     * This method runs all jobs. It's called by a fixed-rate timer in the constructor
     */
    public void runJobs(boolean forced) throws SQLException {
        System.out.println("Running jobs...");
        generateReports(forced);
        checkForLateBorrows(forced);
        sendBookReturnReminders(forced);
        cancelOrders(forced);
        System.out.println("Finished Running jobs...");
    }

    /**
     * This method cancels the order for the book
     * @throws SQLException
     */
    public void cancelOrders(boolean forced) throws SQLException {
        LocalDateTime date = getJobDate("cancel-orders");
        LocalDateTime now = LocalDateTime.now();

        if (forced || date == null || ChronoUnit.HOURS.between(date, now) >= 1) {
            System.out.println("Running job: cancel-orders");
            BookControl.cancelLateOrders();
            markJobDone("cancel-orders");
        }
    }

    /**
     * Sends reminder to return the book
     * @throws SQLException
     */
    public void sendBookReturnReminders(boolean forced) throws SQLException {
        LocalDateTime date = getJobDate("send-reminders");
        LocalDateTime now = LocalDateTime.now();

        if (forced || date == null || ChronoUnit.DAYS.between(date, now) >= 1) {
            System.out.println("Running job: send-reminders");
            final String messageTemplate ="Hi %s,<br>" +
                    "We wanted to remind you that you have to return the book '%s' to the library by tomorrow.<br>" +
                    "BLib Library.";

            ArrayList<Map<String, Object>> records = BookControl.getBooksForReturnReminder();
            for (Map<String, Object> record : records) {
                CommunicationManager.sendMail((String) record.get("email"), String.format("Returning %s", record.get("title")),
                        String.format(messageTemplate, record.get("name"), record.get("title")), "BLib Reminders");

                CommunicationManager.sendSMS((String)record.get("phone_number"), String.format(messageTemplate.replace("<br>", "\n"), record.get("name"), record.get("title")));
            }

            markJobDone("send-reminders");
        }
    }

    /**
     * Tries and generates a report at the end of each month (or start of each month)
     */
    public void generateReports(boolean forced) throws SQLException {
        LocalDateTime date = getJobDate("generate-reports");
        LocalDateTime now = LocalDateTime.now();
        LocalDate lastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);

        if (forced || date == null || ChronoUnit.DAYS.between(date, now) > 31 || !date.getMonth().equals(now.getMonth())) {
            System.out.println("Running job: generate-reports");
            // The moment that the next month enters, the last month "ends".
            // We want to also ensure that the report is generated in case the app isn't on by the 1st of the month.
            generateSubscriberStatusReport(lastMonth);
            generateBorrowTimesReport(lastMonth);

            markJobDone("generate-reports");
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
                "WHERE action = 'FREEZE_SUBSCRIBER' AND (date >= ? OR end_date <= ?)";
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

    /**
     * Generates borrow times to display on screen
     * @param date
     */
    public void generateBorrowTimesReport(LocalDate date) {
        String joinLate = "LEFT JOIN subscriber_history AS late ON " +
                "late.book_copy_id = borrow.book_copy_id AND late.action = 'LATE_RETURN' " +
                "AND late.date = borrow.end_date AND late.subscriber_id = borrow.subscriber_id ";

        String query = "SELECT book_copy.book_id AS book_id, borrow.*, late.date AS late_date, late.end_date AS late_return_date " +
                "FROM subscriber_history AS borrow " +
                "JOIN book_copy ON book_copy_id = book_copy.id " +
                joinLate +
                "WHERE borrow.action = 'BORROW_BOOK' " +
                "AND (MONTH(borrow.date) = MONTH(?) OR MONTH(borrow.end_date) = MONTH(?) OR MONTH(late.end_date) = MONTH(?)) ";
        try (PreparedStatement st = DBControl.prepareStatement(query)) {
            Date sqlDate = Date.valueOf(date);
            st.setDate(1, sqlDate);
            st.setDate(2, sqlDate);
            st.setDate(3, sqlDate);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String query2 = "INSERT INTO borrow_report (book_id, book_copy_id, start_date, return_date, is_late, late_return_date, report_date) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement st2 = DBControl.prepareStatement(query2)) {
                    int bookId = rs.getInt("book_id");
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
    public void checkForLateBorrows(boolean forced) throws SQLException {
        LocalDateTime date = getJobDate("check-borrows");
        LocalDateTime now = LocalDateTime.now();

        if (forced || date == null || ChronoUnit.HOURS.between(date, now) >= 1) {
            System.out.println("Running job: check-borrows");
            // Punish week+ late returns
            String query = "SELECT book_copy.borrow_subscriber_id FROM book_copy " +
                    "LEFT JOIN subscriber AS s ON book_copy.borrow_subscriber_id = s.id " +
                    "WHERE DATE_ADD(book_copy.return_date, INTERVAL 1 WEEK) < NOW() " +
                    "AND is_late = 1 " +
                    "AND (s.frozen_until IS NULL OR s.frozen_until < NOW())";

            try (Statement st = DBControl.createStatement()) {
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    SubscriberControl.freezeSubscriber(rs.getInt("borrow_subscriber_id"));
                }
            }

            // Make an entry in the history for being late
            String lateCheckQuery = "SELECT book_copy.id, borrow_subscriber_id FROM book_copy " +
                    "LEFT JOIN subscriber AS s ON borrow_subscriber_id = s.id " +
                    "WHERE book_copy.return_date < NOW() AND is_late = 0";

            try (Statement st = DBControl.createStatement()) {
                ResultSet rs = st.executeQuery(lateCheckQuery);
                if (rs.next()) {
                    int id = rs.getInt("id");
                    SubscriberControl.logIntoHistory(
                        new HistoryEntry(rs.getInt("borrow_subscriber_id"), HistoryAction.LATE_RETURN, id)
                    );

                    String updateCopyAsLateQuery = "UPDATE book_copy SET is_late = 1 WHERE id = ?";
                    try (PreparedStatement ps = DBControl.prepareStatement(updateCopyAsLateQuery)) {
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    }
                }

            }

            markJobDone("check-borrows");
        }
    }

    /**
     * Returns the last time the job got executed. Null if it never was.
     * @param jobName
     * @return Date
     * @throws SQLException
     */
    public LocalDateTime getJobDate(String jobName) throws SQLException {
        try (PreparedStatement ps = DBControl.prepareStatement("SELECT * FROM job WHERE name = ?")) {
            ps.setString(1, jobName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("date").toLocalDateTime();
            }
        }
        return null;
    }

    /**
     * Marks a job as done by setting its date to the current time
     * @param jobName
     * @throws SQLException
     */
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
