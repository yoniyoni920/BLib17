package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entities.Subscriber;

public class SubscriberControl {
	public static void updateInfo(String[] changedInfo) {
		try {
			PreparedStatement stt = DBControl.getConnection().prepareStatement("UPDATE blib.subscriber SET " +
					"subscriber_phone_number = ?, subscriber_email = ? WHERE subscriber_id = ?");
			stt.setString(1,changedInfo[0]);
			stt.setString(2,changedInfo[1]);
			stt.setString(3, changedInfo[2]);
			stt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		} 
	}
}
