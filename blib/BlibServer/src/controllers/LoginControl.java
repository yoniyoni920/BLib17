package controllers;

import java.sql.*;

import entities.*;

/*
 * The class handles login logic for users and subscribers.
 * It verifies credentials and returns the appropriate user type based on the role.
 */
public class LoginControl {
	 /**
     * Handles the login action by verifying the user's credentials.
     * Based on the role of the user, it returns either a Subscriber or a User object.
     * 
     * @param id the login ID provided by the user
     * @param password the password provided by the user
     * @return a User or Subscriber object if credentials are valid, otherwise null
     */
	public static User login(int id, String password) {
		String query = "SELECT user.*, subscriber.*, subscriber.id AS subscriber_id FROM user " +
				"LEFT JOIN subscriber ON subscriber.user_id = user.id " +
				"WHERE user.id = ? AND user.password = ?";
		try (PreparedStatement ps = DBControl.prepareStatement(query)) {
			ps.setInt(1, id);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			if (!rs.next()) {
				return null;
			}

			// Get user values
			String role = rs.getString("role");
			String name = rs.getString("first_name");
			String lastName = rs.getString("last_name");

			if (role.equals("subscriber")) {
				// Get subscriber-specific values
				String phoneNumber = rs.getString("phone_number");
				String email = rs.getString("email");
				Date date = rs.getDate("frozen_until");

				Subscriber subscriber = new Subscriber(
					rs.getInt("subscriber_id"),
					id,
					name,
					lastName,
					role,
					password,
					phoneNumber,
					email,
					date != null ? date.toLocalDate() : null
				);

				subscriber.setHistory(SubscriberControl.getSubscriberHistory(id));
				subscriber.setBorrowedBooks(BookControl.retrieveBorrowedBooks(id));

				SubscriberControl.logIntoHistory(new HistoryEntry(
					rs.getInt("subscriber_id"),
						HistoryAction.LOGIN_SUBSCRIBER
				));

				return subscriber;
			} else {
				return new User(id, name, lastName, role, password);
			}
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}

	/**
	 * Registers a subscriber in the system by inserting data into both the `user` and `subscriber` tables.
	 *
	 * @param firstName the first name of the user
	 * @param lastName the last name of the user
	 * @param password the password of the user
	 * @param phoneNumber the phone number of the subscriber
	 * @param email the email of the subscriber
	 * @return a Message object containing the result of the registration, including the user object if successful
	 */
	public static User register(String firstName, String lastName, String password, String phoneNumber, String email) {
		String userQuery = "INSERT INTO user (first_name, last_name, password, role) VALUES (?, ?, ?, ?)";
		Connection conn = DBControl.getConnection();

		try (PreparedStatement ps = conn.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
			// Step 2: Insert into `user` table
			int generatedId;

			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setString(3, password);
			ps.setString(4, "subscriber"); // Role is always "subscriber"

			int userRows = ps.executeUpdate();

			if (userRows == 0) {
				throw new SQLException("Failed to insert into user table, no rows affected.");
			}

			ResultSet generatedKeys = ps.getGeneratedKeys();
			if (generatedKeys.next()) {
				generatedId = generatedKeys.getInt(1); // Get the auto-incremented ID
			} else {
				throw new SQLException("Failed to retrieve user ID.");
			}

			// Step 3: Insert into `subscriber` table
			String subscriberQuery = "INSERT INTO subscriber (user_id, phone_number, email, frozen_until) VALUES ( ?, ?, ?, ?)";
			try (PreparedStatement subscriberStmt = conn.prepareStatement(subscriberQuery)) {
				//subscriberStmt.setInt(1, Integer.valueOf(Id)); // Use the same ID as in the `user` table
				subscriberStmt.setInt(1, generatedId); // `user_id` also matches `id` in the `user` table
				subscriberStmt.setString(2, phoneNumber);
				subscriberStmt.setString(3, email);
				subscriberStmt.setNull(4, java.sql.Types.TIMESTAMP); // `frozen_until` is null

				int subscriberRows = subscriberStmt.executeUpdate();
				if (subscriberRows == 0) {
					throw new SQLException("Failed to insert into subscriber table, no rows affected.");
				}
			}

			// Return a new User object (assuming you have a User class)
			return new User(generatedId, firstName, lastName, password, "subscriber");
		} catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
}
