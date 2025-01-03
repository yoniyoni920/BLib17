package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
