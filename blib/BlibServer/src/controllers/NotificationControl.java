package controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entities.Notification;

/**
 * This class provides methods to manage notifications in the system.
 * It includes functionality to retrieve, save, and update notification statuses in the database.
 */
public class NotificationControl {

    /**
     * Retrieves all notifications from the database.
     * 
     * @return a list of notifications
     */
    public static List<Notification> retrieveNotifications() {
        List<Notification> notifications = new ArrayList<>();
        ResultSet rs;
        
        //retrieving notifications from the DB
        try {
            Statement stmt = DBControl.createStatement();
            rs = stmt.executeQuery("SELECT * FROM notification ORDER BY date DESC");
            while (rs.next()) {
                Notification notification = new Notification(
                        rs.getInt("subscriber_id"),
                        rs.getString("subscriber_name"),
                        rs.getString("message"),
                        rs.getDate("date").toLocalDate(),
                        rs.getBoolean("isNew")
                );
                notification.setNotificationId(rs.getInt("id"));
                notifications.add(notification);
            }
        } catch (SQLException e) {
        	System.out.println("Couldn't retrieve the notifications from the DB");
            e.printStackTrace();
            return null ;
        }
        
        return notifications;
    }

    /**
     * Saves a new notification to the database.
     * 
     * @param notification the notification to be saved
     * @return true if the notification was saved successfully, false otherwise
     */
    public static boolean saveNotification(Notification notification) {
        int rowsAffected;
        try {
        	
        	//preparing a statement
        	String sqlQuery = "INSERT INTO notification (subscriber_id, subscriber_name , message, date, isNew) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = DBControl.prepareStatement(sqlQuery);
            
            //inserting the variables in the query
            stmt.setString(1, notification.getSubscriberId() + "");
            stmt.setString(2, notification.getSubscriberName());
            stmt.setString(3, notification.getMessage());
            stmt.setString(4, notification.getDate().toString());
            if (notification.getIsNew())
                stmt.setString(5, 1 + "");
            else
                stmt.setString(5, 0 + "");

            //executing the query
            rowsAffected = stmt.executeUpdate();
            
            
        } catch (SQLException e) {
			System.out.println("Couldn't save notification in the DB : NotificationControl.saveNotification()");
            e.printStackTrace();
            return false;
        }
        
        
        return rowsAffected > 0;
    }

    /**
     * Updates the status of the given notifications to 'read' (isNew = 0) in the database.
     * 
     * @param notifications the list of notifications to be updated
     * @return true if the status update was successful for all notifications, false otherwise
     */
    public static boolean updateNotificationStatus(List<Notification> notifications) {
    	
    	//creating statement
        Statement stmt;
		try {
			stmt = DBControl.createStatement();
		} catch (SQLException e) {
			System.out.println("Couldn't create a statement in : NotificationControl.updateNotificationStatus()");
			e.printStackTrace();
			return false ;
		}
		
		//creating a batch update
        for (Notification notification : notifications) {
            try {
                String query = "UPDATE notification SET isNew = 0 WHERE id = " + notification.getNotificationId();
                stmt.addBatch(query);
            } catch (SQLException e) {
    			System.out.println("Error in creating the batch update in : NotificationControl.updateNotificationStatus()");
                e.printStackTrace();
                return false;
            }
        }
        
        //executing the batch
        try {
            stmt.executeBatch();
        } catch (SQLException e) {
			System.out.println("Error in executing the batch update in : NotificationControl.updateNotificationStatus()");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
