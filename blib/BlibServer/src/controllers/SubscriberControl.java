package controllers;

import java.sql.*;
import java.time.LocalDate;


import entities.Subscriber;
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
	public static Subscriber getSubscriberById(int id) {
		try {
			try(PreparedStatement stt = DBControl
					.getConnection()
					.prepareStatement("SELECT phone_number, email, frozen_until, role, first_name, last_name FROM blib.subscriber join blib.user on user.id=subscriber.user_id where user_id=?")
			) {
				stt.setInt(1,id);
				ResultSet result = stt.executeQuery();
				if(result.next()) {
					String phoneNumber = result.getString("phone_number");
					String email = result.getString("email");
					Date frozenUntil = result.getDate("frozen_until");
					String role = result.getString("role");
					String firstName = result.getString("first_name");
					String lastName = result.getString("last_name");
					String status;
					if(frozenUntil == null || frozenUntil.before(Date.valueOf(LocalDate.now()))) {
						status = "valid";
					}
					else {
						status = "frozen";
					}
					Subscriber sub = new Subscriber(String.valueOf(id), firstName, lastName, role, phoneNumber, email);
					sub.setStatus(status);
					return sub;
				}

			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
}
