package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Saves the notification information to display
 */
public class Notification implements Serializable{
	private int id;
	private int subscriberId;
	private int userId;
	private String subscriberName;
	private String message;
	private LocalDateTime date;
	private boolean isNew;
	
	public Notification(int subscriberId, int userId, String message, LocalDateTime date, boolean isNew) {
		this.subscriberId = subscriberId;
		this.userId = userId;
		this.message = message;
		this.date = date;
		this.isNew = isNew;
	}

	public Notification(int subscriberId, String message) {
		this.subscriberId = subscriberId;
		this.message = message;
		this.date = LocalDateTime.now();
		this.isNew = true;
	}

	public int getNotificationId() {
		return id ;
	}
	
	public void setNotificationId(int id) {
		this.id = id ;
	}
	
	public int getSubscriberId() {
		return subscriberId ;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public boolean getIsNew() {
		return isNew;
	}
	
	public void setIsNew(boolean isNew) {
		this.isNew = isNew;
	}
	
	public String getSubscriberName() {
		return subscriberName ;
	}

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }
}
