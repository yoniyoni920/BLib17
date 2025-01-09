package entities;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/*
 * Subscribers are the members of the library
 */
public class Subscriber extends User implements Serializable {
	private String status;
	private String phoneNumber;
	private String email;
	private LocalDate frozenUntil;

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
		LocalDate frozenUntil
	) {
		super(id, name, lastName, role);
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.frozenUntil = frozenUntil;
	}

	public String getStatus() {
		return status;
	}

	public boolean isFrozen() {
		return frozenUntil == null || frozenUntil.isBefore(LocalDate.now());
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
