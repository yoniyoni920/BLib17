package entities;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/*
 * Subscribers are the members of the library
 */
public class Subscriber extends User implements Serializable {
	private String phoneNumber;
	private String email;

	private LocalDate frozenUntil;
	private DetailedSubscriptionHistory detailedSubscriptionHistory;

	private List<BookCopy> borrowedBooks;

	public Subscriber() {
		super();
	}

	public Subscriber(
		int id,
		String firstName,
		String lastName,
		String role,
		String password,
		String phoneNumber,
		String email,
		LocalDate frozenUntil
	) {
		super(id, firstName, lastName, role, password);
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.frozenUntil = frozenUntil;
	}

	public boolean isFrozen() {
		return frozenUntil != null && frozenUntil.isAfter(LocalDate.now());
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
	public void setDetailedSubscriptionHistory(DetailedSubscriptionHistory detailedSubscriptionHistory) {
		this.detailedSubscriptionHistory = detailedSubscriptionHistory;
	}

	public LocalDate getFrozenUntil() {
		if(isFrozen())
			return frozenUntil;
		return null;
	}

	public void setFrozenUntil(LocalDate frozenUntil) {
		this.frozenUntil = frozenUntil;
	}

	public List<BookCopy> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BookCopy> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	@Override
	public String toString() {
		return "Subscriber : " + super.getName();
	}
}
