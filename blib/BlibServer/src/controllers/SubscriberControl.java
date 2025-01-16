package controllers;

import java.sql.*;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import entities.BookCopy;
import entities.Subscriber;
import entities.SubscriberStatusReport;

/*
 * This class is the control  for Subscriber
 */
public class SubscriberControl {
	/*
	 * changes the information on the DB to input.
	 */
	public static void updateInfo(List<String> changedInfo) {
		try {
			PreparedStatement subscriberStatement = DBControl.getConnection().prepareStatement("UPDATE subscriber SET phone_number = ?, email = ? WHERE user_id = ?");
			PreparedStatement userStatement = DBControl.getConnection().prepareStatement("UPDATE user SET first_name = ?, last_name = ?, password = ? WHERE id = ?");
			subscriberStatement.setString(1,changedInfo.get(3));
			subscriberStatement.setString(2,changedInfo.get(4));
			subscriberStatement.setString(3, changedInfo.get(0));
			subscriberStatement.executeUpdate();
			userStatement.setString(1, changedInfo.get(1));
			userStatement.setString(2, changedInfo.get(2));
			userStatement.setString(3, changedInfo.get(5));
			userStatement.setString(4, changedInfo.get(0));
			userStatement.executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println("Error in updating the user data in the DataBase");
		} 
	}
	public static Subscriber getSubscriberById(int id) {
		try {
			String query = "SELECT * FROM subscriber join user on user.id=subscriber.user_id where user_id=?";
			try(PreparedStatement stt = DBControl.getConnection().prepareStatement(query)) {
				stt.setInt(1,id);
				ResultSet rs = stt.executeQuery();
				if(rs.next()) {
					return getSubscriberFromResultSet(rs);
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns an object of a subscriber using a ResultSet
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static Subscriber getSubscriberFromResultSet(ResultSet rs) throws SQLException {
		int id = rs.getInt("id");
		String phoneNumber = rs.getString("phone_number");
		String email = rs.getString("email");
		Date frozenUntil = rs.getDate("frozen_until");
		String role = rs.getString("role");
		String firstName = rs.getString("first_name");
		String lastName = rs.getString("last_name");

		Subscriber sub = new Subscriber(
			id,
			firstName,
			lastName,
			role,
			null,
			phoneNumber,
			email,
			frozenUntil != null ? frozenUntil.toLocalDate() : null
		);

		sub.setBorrowedBooks(BookControl.retrieveBorrowedBooks(id));

		return sub;
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
