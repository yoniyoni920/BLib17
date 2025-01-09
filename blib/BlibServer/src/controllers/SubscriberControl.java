package controllers;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entities.BorrowReport;
import entities.Subscriber;
import entities.SubscriberStatusReport;

/*
 * This class is the control  for Subscriber
 */
public class SubscriberControl {
	/*
	 * changes the information on the DB to input.
	 */
	public static void updateInfo(String[] changedInfo) {
		try {
			try(PreparedStatement stt = DBControl
					.getConnection()
					.prepareStatement("UPDATE subscriber SET phone_number = ?, email = ? WHERE user_id = ?")
			) {
				stt.setString(1,changedInfo[0]);
				stt.setString(2,changedInfo[1]);
				stt.setString(3, changedInfo[2]);
				stt.executeUpdate();
			}
		} catch(SQLException e){
			e.printStackTrace();
		} 
	}

	/**
	 * Returns a list of dates in which a report has been generated.
	 * Enough that there is a single case made in the report for it to count as a report
	 *
	 * @return List<LocalDate
	 */
	public static List<LocalDate> getReportDates() {
		// Could use any report type, both of them are generated at the same time
		String query = "SELECT report_date FROM subscriber_status_report GROUP BY report_date";

		try (Statement st = DBControl.createStatement()) {
			ResultSet rs = st.executeQuery(query);

			List<LocalDate> reportDates = new ArrayList<>();

			while (rs.next()) {
				reportDates.add(rs.getDate("report_date").toLocalDate());
			}

			return reportDates;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns an array that contains each day of the month and how many frozen subscribers
	 * were there at that day
	 *
	 * @return int[]
	 */
	public static int[] getSubscriberStatusReport(LocalDate date) {
		String query = "SELECT * FROM subscriber_status_report WHERE report_date = ?";

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			st.setObject(1, date);
			ResultSet rs = st.executeQuery();
			List<SubscriberStatusReport> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new SubscriberStatusReport(
					rs.getDate("report_date").toLocalDate(),
					rs.getDate("freeze_date").toLocalDate(),
					rs.getDate("freeze_end_date").toLocalDate()
				));
			}

			int[] statusCount = new int[31];
			for (int i = 0; i < 31; i++) {
				LocalDate day = date.plusDays(i);
				for (SubscriberStatusReport report : list) {
					// Collect all freezes where this day of the month is within the freeze duration
					if (day.isAfter(report.getFreezeDate()) && (day.isBefore(report.getFreezeEndDate()) || day.isEqual(report.getFreezeEndDate()))) {
						statusCount[i]++;
					}
				}
			}

			return statusCount;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
