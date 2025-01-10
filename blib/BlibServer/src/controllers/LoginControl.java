package controllers;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

import entities.DetailedSubscriptionHistory;
import entities.Subscriber;
import entities.User;
/*
 * The class handles login logic. 
 * 
 */
public class LoginControl {
	//need to add later that privilege will detect
	//type of user and return the correct obj type
	//--after adding status can add the option to check
	//if user is already logged in and if so make sure 
	//he can't log in again and display warning
	
	/*
	 * Login to the Database checks
	 * for the user and starts the session.
	 */
	public static User loginAction(String loginId, String loginPassword) {
		try {
			System.out.println("i am here");
			// This is called the try-with-resource block, it automatically closes the prepared statement and result set when it's done.
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
				String id = rs.getString("id");
				String name = rs.getString("first_name");
				String lastName = rs.getString("last_name");

				System.out.println("I am here 2 xd");

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

						DetailedSubscriptionHistory history = new DetailedSubscriptionHistory();
						history.setActionsHistory(DetailedSubscriptionHistoryControl.retrieveActionsHistory());
						subscriber.setDetailedSubscriptionHistory(history);
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
