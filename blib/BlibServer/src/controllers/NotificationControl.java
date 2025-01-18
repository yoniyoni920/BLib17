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
        try {
            Statement stmt = DBControl.createStatement();
            rs = stmt.executeQuery("SELECT * FROM notification");
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
            e.printStackTrace();
            return notifications;
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
            PreparedStatement stmt = DBControl.prepareStatement("INSERT INTO notification (subscriber_id, subscriber_name , message, date, isNew) VALUES (?,?,?,?,?)");
            stmt.setString(1, notification.getSubscriberId() + "");
            stmt.setString(2, notification.getSubscriberName());
            stmt.setString(3, notification.getMessage());
            stmt.setString(4, notification.getDate().toString());
            if (notification.getIsNew())
                stmt.setString(5, 1 + "");
            else
                stmt.setString(5, 0 + "");

            rowsAffected = stmt.executeUpdate();
        } catch (SQLException e) {
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
        Statement stmt = null;
        for (Notification notification : notifications) {
            try {
                stmt = DBControl.createStatement();
                String query = "UPDATE notification SET isNew = 0 WHERE id = " + notification.getNotificationId();
                stmt.addBatch(query);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
