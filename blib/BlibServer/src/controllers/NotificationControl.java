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

        //retrieving notifications from the DB
        try(Statement stmt = DBControl.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT *, user.first_name AS subscriber_name FROM notification " +
                    "JOIN subscriber ON subscriber.id = subscriber_id " +
                    "JOIN user ON user.id = subscriber.user_id " +
                    "ORDER BY date DESC");
            while (rs.next()) {
                Notification notification = new Notification(
                    rs.getInt("subscriber_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getBoolean("is_new")
                );
                notification.setSubscriberName(rs.getString("subscriber_name"));
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
        String sqlQuery = "INSERT INTO notification (subscriber_id, message, date, is_new) VALUES (?,?,?,?)";

        try(PreparedStatement stmt = DBControl.prepareStatement(sqlQuery)) {
            //inserting the variables in the query
            stmt.setString(1, notification.getSubscriberId() + "");
            stmt.setString(2, notification.getMessage());
            stmt.setString(3, notification.getDate().toString());
            stmt.setBoolean(4, notification.getIsNew());

            //executing the query
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
			System.out.println("Couldn't save notification in the DB : NotificationControl.saveNotification()");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates the status of all notifications to 'read' (is_new = 0) in the database.
     * 
     * @param notifications the list of notifications to be updated
     * @return true if the status update was successful for all notifications, false otherwise
     */
    public static boolean updateNotificationStatus(List<Notification> notifications) {
		try (PreparedStatement st = DBControl.prepareStatement("UPDATE notification SET is_new = 0 WHERE id = ?")) {
            for (Notification notification : notifications) {
                st.setInt(1, notification.getNotificationId());
                st.addBatch();
            }
            st.executeBatch();
		} catch (SQLException e) {
			System.out.println("Couldn't create a statement in : NotificationControl.updateNotificationStatus()");
			e.printStackTrace();
			return false ;
		}
        return true;
    }
}
