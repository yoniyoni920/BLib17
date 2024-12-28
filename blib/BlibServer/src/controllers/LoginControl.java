package controllers;

import java.sql.*;
import java.util.HashMap;

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
	public static Subscriber loginAction(String loginId){
		try {
			// This is called the try-with-resource block, it automatically closes the prepared statement and result set when it's done.
			try (PreparedStatement ps = DBControl.getInstance().selectQuery("subscriber", "subscriber_id", loginId);
				ResultSet rs = ps.executeQuery()
			) {
				if(rs.next()) {
					String id = rs.getString("subscriber_id");
					String subscriberName = rs.getString("subscriber_name");
					int history = rs.getInt("detailed_subscription_history");
					String phoneNumber = rs.getString("subscriber_phone_number");
					String email = rs.getString("subscriber_email");
					return new Subscriber(id, subscriberName, history, phoneNumber, email);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
