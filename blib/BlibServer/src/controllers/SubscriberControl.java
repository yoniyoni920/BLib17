package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import entities.BookCopy;
import entities.Subscriber;
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
	
	
}
