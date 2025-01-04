package base;

import controllers.DBControl;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    }

    /**
     * This method runs all jobs. It's called by a fixed-rate timer in the constructor
     */
    public void runJobs() {
        generateReports();
        checkForLateBorrows();
    }

    /**
     * Tries and generates a report at the end of each month (or start of each month)
     */
    public void generateReports() {
        LocalDateTime date = getJobDate("generate-reports");
        LocalDateTime now = LocalDateTime.now();

        if (date == null || !date.getMonth().equals(now.getMonth())) {
            // The moment that the next month enters, the last month "ends".
            // We want to also ensure that the report is generated in case the app isn't on by the 1st of the month.

            try {
                markJobDone("generate-reports");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks for any late return. If it detects a week late return it punishes the offending subscriber
     * by freezing their account for a month (30 days)
     */
    public void checkForLateBorrows() {
        LocalDateTime date = getJobDate("check-borrows");
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Did check for late reports " + ChronoUnit.HOURS.between(date, now) + " hours ago");
        System.out.println("Now : " + now + " date: " + date);

        if (ChronoUnit.HOURS.between(date, now) >= 1) {
            String query = "SELECT book_copy.*, s.* FROM book_copy " +
                    "LEFT JOIN subscriber AS s ON book_copy.borrow_subscriber_id = s.id " +
                    "WHERE DATE_ADD(book_copy.return_date, INTERVAL 1 WEEK) < NOW()" +
                    "AND (s.frozen_until IS NULL OR s.frozen_until < NOW())";

            try (Statement st = DBControl.getConnection().createStatement()) {
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    try (PreparedStatement st2 = DBControl.getConnection()
                            .prepareStatement("UPDATE subscriber SET frozen_until = ? WHERE id = ?")
                    ) {
                        st2.setTimestamp(1, Timestamp.valueOf(now.plusDays(30)));
                        st2.setInt(2, rs.getInt("borrow_subscriber_id"));
                        st2.execute();
                    }
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
            return null;
        }
        return null;
    }

    public void markJobDone(String jobName) throws SQLException {
        PreparedStatement st;
        if (getJobDate(jobName) == null) {
            st = DBControl.getConnection().prepareStatement("INSERT INTO job (date, name) VALUES (now(), ?)");
        } else {
            st = DBControl.getConnection().prepareStatement("UPDATE job SET date = now() WHERE name = ?");
        }
        st.setString(1, jobName);
        st.execute();
        st.close();
        System.out.println("Hi!!! " + jobName);
    }
}
