package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/*
 * Subscribers are the members of the library
 */
public class Subscriber extends User implements Serializable {
	private String phoneNumber;
	private String email;

	private LocalDate frozenUntil;
	private List<HistoryEntry> history;

	private List<BookCopy> borrowedBooks;

	private int subscriberId;

	public Subscriber() {
		super();
	}

	public Subscriber(
		int subscriberId,
		int userId,
		String firstName,
		String lastName,
		String role,
		String password,
		String phoneNumber,
		String email,
		LocalDate frozenUntil
	) {
		super(userId, firstName, lastName, role, password);
		this.subscriberId = subscriberId;
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

	public List<HistoryEntry> getHistory() {
		return history;
	}

	public void setHistory(List<HistoryEntry> history) {
		this.history = history;
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
		return getName() + " (" + getId() + " )";
	}

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }
}
