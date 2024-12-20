package entities;

import java.io.Serializable;

public class Subscriber extends User implements Serializable {
	private String status;
	private int detailedSubscriptionHistory;
	private String phoneNumber;
	private String email;
	public Subscriber() {
		super();
	}
	public Subscriber(String id, String subscriberName, int history, String phoneNumber, String email) {
		super(id, subscriberName);
		this.detailedSubscriptionHistory = history;
		this.phoneNumber = phoneNumber;
		this.email=email;
		this.status = "Not Frozen";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public int getDetailedSubscriptionHistory() {
		return detailedSubscriptionHistory;
	}
	public void setDetailedSubscriptionHistory(int detailedSubscriptionHistory) {
		this.detailedSubscriptionHistory = detailedSubscriptionHistory;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
