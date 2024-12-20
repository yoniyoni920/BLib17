package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	public static Subscriber loginAction(String logger){
		try {
			
			System.out.println("Login action connected to the database successfully ");
			Statement stt = DBControl.getConnection().createStatement();
			ResultSet rs =  stt.executeQuery("SELECT * FROM blib.subscriber WHERE subscriber_id = '"+ logger+ "'");
		
		    Subscriber subscriber = null;
		    boolean found = false;
		    if(rs.next()) {
				found = true;
				String id = rs.getString("subscriber_id");
				String subscriberName = rs.getString("subscriber_name");
				int history = rs.getInt("detailed_subscription_history");
				String phoneNumber = rs.getString("subscriber_phone_number");
				String email = rs.getString("subscriber_email");
				subscriber = new Subscriber(id, subscriberName, history, phoneNumber, email);	
			}
		    
		    rs.close();
            stt.close();
		    
			return found ? subscriber : null;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}
}
