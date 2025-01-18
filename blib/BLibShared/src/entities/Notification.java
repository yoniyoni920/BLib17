package entities;

import java.io.Serializable;
import java.time.LocalDate;

public class Notification implements Serializable{
	private int id ;
	private int subscriberId ;
	private String subscriberName;
	private String message ;
	private LocalDate date ;
	private boolean isNew ;
	
	public Notification(int subscriberId ,String subscriberName , String message , LocalDate date , boolean isNew) {
		this.subscriberId = subscriberId;
		this.subscriberName = subscriberName;
		this.message = message;
		this.date = date ;
		this.isNew = isNew ;
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
	
	public String getMessage() {
		return message;
	}
	
	public LocalDate getDate() {
		return date ;
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
}
