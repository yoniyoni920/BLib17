package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import client.entities.Subscriber;
import client.entities.User;

public class LoginControl {
	// need to add later that privilege will detect type of user and return the correct obj type
	public static Subscriber loginAction(User logger){
		try {
			
			System.out.println("Login action connected to the database successfully ");
			Statement stt = DBControl.getConnection().createStatement();
			ResultSet rs =  stt.executeQuery("SELECT Id,FirstName,LastName FROM " +
					"Subscriber WHERE" +
					"Id = '"+ logger.getId()+ "'");
			
//			ResultSet rs =  stt.executeQuery("SELECT Id,FirstName,LastName,Password,Privilege FROM " +
//					"Subscriber WHERE password = '" + logger.getPassword() + "'"+
//					"And Id = "+ logger.getId()+ "'");
//			
//			
			
			
			
			
		    Subscriber subscriber = null;
		    boolean found = false;
		    if(rs.next()) {
				found = true;
				String Id = rs.getString("Id");
				String fName = rs.getString("FirstName");
				String lName = rs.getString("LastName");
				String privilige = rs.getString("Privilege");
				subscriber = new Subscriber(Id, fName, lName);	
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
