package controllers;

import java.sql.*;

import entities.Subscriber;
import entities.User;
/*
 * The class handles login logic for users and subscribers.
 * It verifies credentials and returns the appropriate user type based on the role.
 */
public class LoginControl {
	 /**
     * Handles the login action by verifying the user's credentials.
     * Based on the role of the user, it returns either a Subscriber or a User object.
     * 
     * @param loginId the login ID provided by the user
     * @param loginPassword the password provided by the user
     * @return a User or Subscriber object if credentials are valid, otherwise null
     */
	public static User loginAction(String loginId, String loginPassword) {
		try {
			
			System.out.println("i am here");
			// This is called the try-with-resource block, it automatically closes the prepared statement and result set when it's done.
			System.out.println("login:" + loginId + "password:" + loginPassword);
			try (PreparedStatement ps = DBControl
					.getInstance()
					.selectQuery("user","id", loginId, "password", loginPassword)
			) {
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) {
					return null;
				}

				// Get user values
				String role = rs.getString("role");
				int id = rs.getInt("id");
				String name = rs.getString("first_name");
				String lastName = rs.getString("last_name");

				if (role.equals("subscriber")) {
					try (PreparedStatement ps2 = DBControl.getInstance().selectQuery("subscriber", "user_id", loginId)) {
						ResultSet resultSubscriber = ps2.executeQuery();
						if (!resultSubscriber.next()) {
							return null;
						}

						// Get subscriber-specific values
						String phoneNumber = resultSubscriber.getString("phone_number");
						String email = resultSubscriber.getString("email");
  						Date date = resultSubscriber.getDate("frozen_until");

						Subscriber subscriber = new Subscriber(
							id,
							name,
							lastName,
							role,
							loginPassword,
							phoneNumber,
							email,
							date != null ? date.toLocalDate() : null
						);

						subscriber.setHistory(SubscriberControl.getSubscriberHistory(id));
						subscriber.setBorrowedBooks(BookControl.retrieveBorrowedBooks(subscriber.getId()));
						return subscriber;
					}
				} else {
					return new User(id, name, lastName, role, loginPassword);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
