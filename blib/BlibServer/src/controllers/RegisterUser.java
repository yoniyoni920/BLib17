package controllers;

import java.sql.*;
import java.util.HashMap;

import entities.Message;
import entities.Subscriber;
import entities.User;
/*
 * The class handles user registration logic.
 * 
 * It registers a new user by inserting data into the `user` and `subscriber` tables in the database.
 */
public class RegisterUser {

	  /**
     * Registers a new user in the system by inserting data into both the `user` and `subscriber` tables.
     * 
     * @param FirstName the first name of the user
     * @param LastName the last name of the user
     * @param Password the password of the user
     * @param PhoneNumber the phone number of the subscriber
     * @param Email the email of the subscriber
     * @return a Message object containing the result of the registration, including the user object if successful
     */
	public static Message registerAction(String FirstName, String LastName, String Password, String PhoneNumber, String Email) {
	    Connection connection = null;
	    
	    try {
	        // Step 1: Get a database connection
	        connection = DBControl.getInstance().getConnection();
	        connection.setAutoCommit(false); // Start a transaction

	        // Step 2: Insert into `user` table
	        String userQuery = "INSERT INTO user (first_name, last_name, password, role) VALUES (?, ?, ?, ?)";
	        int generatedId;

	        try (PreparedStatement userStmt = connection.prepareStatement(userQuery, Statement.RETURN_GENERATED_KEYS)) {
	            userStmt.setString(1, FirstName);
	            userStmt.setString(2, LastName);
	            userStmt.setString(3, Password);
	            userStmt.setString(4, "subscriber"); // Role is always "subscriber"

	            int userRows = userStmt.executeUpdate();

	            if (userRows == 0) {
	                throw new SQLException("Failed to insert into user table, no rows affected.");
	            }

	            try (ResultSet generatedKeys = userStmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    generatedId = generatedKeys.getInt(1); // Get the auto-incremented ID
	                } else {
	                    throw new SQLException("Failed to retrieve user ID.");
	                }
	            }
	        }

	        // Step 3: Insert into `subscriber` table
	        String subscriberQuery = "INSERT INTO subscriber (user_id, phone_number, email, frozen_until) VALUES ( ?, ?, ?, ?)";
	        try (PreparedStatement subscriberStmt = connection.prepareStatement(subscriberQuery)) {
	            //subscriberStmt.setInt(1, Integer.valueOf(Id)); // Use the same ID as in the `user` table
	            subscriberStmt.setInt(1, generatedId); // `user_id` also matches `id` in the `user` table
	            subscriberStmt.setString(2, PhoneNumber);
	            subscriberStmt.setString(3, Email);
	            subscriberStmt.setNull(4, java.sql.Types.TIMESTAMP); // `frozen_until` is null

	            int subscriberRows = subscriberStmt.executeUpdate();

	            if (subscriberRows == 0) {
	                throw new SQLException("Failed to insert into subscriber table, no rows affected.");
	            }
	        }
	        // Commit the transaction if both inserts were successful
	        connection.commit();

	        // Return a new User object (assuming you have a User class)
	        Message msg = new Message();
            msg.setObject(new User(generatedId, FirstName, LastName, Password, "subscriber"));
            return msg;

	    }catch (SQLException rollbackEx) {
            //rollbackEx.printStackTrace();
            Message msg = new Message();
            msg.setObject("Id allready exists in Database");
            msg.setError(true);
            return msg;
        }catch (Exception e) {
	        e.printStackTrace();
	        if (connection != null) {
	          try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} // Rollback transaction on error
	        }
	    } 
	    return null; // Return null if registration fails
	}
	


	
	
	
	
	
}
