package entities;

import java.io.Serializable;
/*
 * Subscribers are the members of the library
 */
public class Subscriber extends User implements Serializable {
	private String status;
	private String phoneNumber;
	private String email;

	public Subscriber() {
		super();
	}

	public Subscriber(
		String id,
		String name,
		String lastName,
		String role,
		String phoneNumber,
		String email
	) {
		super(id, name, lastName, role);
		this.phoneNumber = phoneNumber;
		this.email = email;
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
}
