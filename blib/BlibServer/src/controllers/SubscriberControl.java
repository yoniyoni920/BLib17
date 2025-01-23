package controllers;

import java.sql.*;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import entities.HistoryAction;
import entities.Subscriber;
import entities.HistoryEntry;
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
			PreparedStatement subscriberStatement = DBControl.prepareStatement("UPDATE subscriber SET phone_number = ?, email = ? WHERE user_id = ?");
			PreparedStatement userStatement = DBControl.prepareStatement("UPDATE user SET first_name = ?, last_name = ?, password = ? WHERE id = ?");
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
			String query = "SELECT * FROM subscriber JOIN user ON user.id=subscriber.user_id WHERE user_id=?";
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

	public static List<Subscriber> searchSubscribers(String search, String searchType) {
		try {
			if (searchType.equals("user_id") || searchType.equals("first_name") || searchType.equals("last_name")) {
				String query = "SELECT * FROM subscriber JOIN user ON user.id=subscriber.user_id";
				if (searchType.equals("user_id")) {
					query += " WHERE user_id = ?";
				} else {
					query += " WHERE " + searchType + " LIKE ?";
				}

				try (PreparedStatement stt = DBControl.getConnection().prepareStatement(query)) {
					if (searchType.equals("user_id")) {
						stt.setInt(1, Integer.parseInt(search));
					} else {
						stt.setString(1, "%" + search + "%");
					}

					ResultSet rs = stt.executeQuery();
					List<Subscriber> list = new ArrayList<>();
					while(rs.next()) {
						list.add(getSubscriberFromResultSet(rs));
					}

					return list;
				}
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns a list of dates in which a report has been generated.
	 * Enough that there is a single case made in the report for it to count as a report
	 * <br>
	 * @return List<LocalDate>
	 */
	public static List<LocalDate> getReportDates() {
		// Could use any report type, both of them are generated at the same time
		String query = "SELECT report_date FROM borrow_report GROUP BY report_date";

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
	 * Returns a list of subscriber status report
	 *
	 * @return List<SubscriberStatusReport>
	 */
	public static List<SubscriberStatusReport> getSubscriberStatusReport(LocalDate date, Integer subscriberId) {
		String query = "SELECT subscriber_status_report.*, user.first_name FROM subscriber_status_report " +
				"JOIN subscriber ON subscriber.id = subscriber_status_report.subscriber_id " +
				"JOIN user ON subscriber.user_id = user.id " +
				"WHERE report_date = ? ";

		if (subscriberId != null) {
			query += " AND subscriber_id = ?";
		}

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			st.setObject(1, date);
			if (subscriberId != null) {
				st.setInt(2, subscriberId);
			}
			ResultSet rs = st.executeQuery();
			List<SubscriberStatusReport> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new SubscriberStatusReport(
					rs.getString("first_name"),
					rs.getInt("subscriber_id"),
					rs.getDate("freeze_date").toLocalDate(),
					rs.getDate("freeze_end_date").toLocalDate()
				));
			}

			System.out.println(list.size());

			return list;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Freezes a subscriber to a duration of 30 days. If the subscriber is already frozen,
	 * it'll increase the duration back to 30 days.
	 * @param subscriberId The ID of the subscriber
	 * @return Whether the operation succeeded
	 */
	public static boolean freezeSubscriber(int subscriberId) {
		String query = "UPDATE subscriber SET frozen_until = ? WHERE id = ?";

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			LocalDateTime now = LocalDateTime.now();
			st.setTimestamp(1, Timestamp.valueOf(now.plusDays(30)));
			st.setInt(2, subscriberId);

			// Log into history
			logIntoHistory(new HistoryEntry(subscriberId, HistoryAction.FREEZE_SUBSCRIBER));

			return st.executeUpdate() == 1;
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

	public static List<HistoryEntry> getSubscriberHistory(int subscriberId) {
		String query = "SELECT *, book.title AS book_title, user.first_name AS librarian_name FROM subscriber_history " +
				"LEFT JOIN book_copy ON book_copy.id = book_copy_id " +
				"LEFT JOIN book ON book.id = book_copy.book_id " +
				"LEFT JOIN user ON user.id = librarian_user_id " +
				"WHERE subscriber_id = ? " +
				"ORDER BY date DESC";

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			List<HistoryEntry> list = new ArrayList<>();
			st.setInt(1, subscriberId);

			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				HistoryEntry item = new HistoryEntry(
					rs.getInt("subscriber_id"),
					rs.getInt("id"),
					HistoryAction.valueOf(rs.getString("action")),
					rs.getTimestamp("date").toLocalDateTime()
				);

				// Set optional fields
				Timestamp endDate = rs.getTimestamp("end_date");
				String bookTitle = rs.getString("book_title");
				String librarianName = rs.getString("librarian_name");

				if (endDate != null) {
					item.setEndDate(rs.getTimestamp("end_date").toLocalDateTime());
				}

				if (bookTitle != null) {
					item.setBookName(rs.getString("book_title"));
				}

				if (librarianName != null) {
					item.setLibrarianName(librarianName);
				}

				list.add(item);
			}

			return list;
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 *
	 * @param historyItem
	 * @return
	 * @throws SQLException
	 */
	public static boolean logIntoHistory(HistoryEntry historyItem) throws SQLException {
		String query = "INSERT INTO subscriber_history (action, subscriber_id, book_copy_id, book_id, date, end_date, librarian_user_id) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			st.setString(1, historyItem.getAction().toString());
			st.setInt(2, historyItem.getSubscriberId());
			st.setObject(3, historyItem.getBookCopyId());
			st.setObject(4, historyItem.getBookId());
			st.setTimestamp(5, Timestamp.valueOf(historyItem.getDate()));

			LocalDateTime endDate = historyItem.getEndDate();
			if (endDate != null) {
				st.setTimestamp(6, Timestamp.valueOf(historyItem.getEndDate()));
			} else {
				st.setTimestamp(6, null);
			}

			st.setObject(7, historyItem.getLibrarianUserId());

			return st.executeUpdate() == 1;
		}
	}
}
