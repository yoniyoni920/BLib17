package entities;

import java.io.Serializable;
/*
 * Subscribers are the members of the library
 */
public class Subscriber extends User implements Serializable {
	private String status;
	private String phoneNumber;
	private String email;
	private DetailedSubscriptionHistory detailedSubscriptionHistory;
	public Subscriber() {
		super();
	}

	public Subscriber(
		String id,
		String name,
		String lastName,
		String role,
		String phoneNumber,
		String email,
		String password,
		DetailedSubscriptionHistory detailedSubscriptionHistory
	) {
		super(id, name, lastName, role, password);
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.detailedSubscriptionHistory = detailedSubscriptionHistory;
	}

	public String getStatus() {
		return status;
	}

	public boolean isFrozen() { //TODO: implement via a date check of frozenUntil
		return false;
	}

	public void setStatus(String status) {
		this.status = status;
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
	public DetailedSubscriptionHistory getDetailedSubscriptionHistory() {
		return detailedSubscriptionHistory ;
	}
	@Override
	public String toString() {
		return "Subscriber : " + super.getName();
	}
}
