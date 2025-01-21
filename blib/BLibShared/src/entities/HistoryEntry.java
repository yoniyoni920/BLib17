package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class HistoryEntry implements Serializable {
	private int id;
	private String action;
	private int subscriberId;
	private int bookCopyId;
	private LocalDateTime date;
	private LocalDateTime endDate;

	private String book;

	public HistoryEntry(int subscriberId, String action, int bookCopyId, LocalDateTime date, LocalDateTime endDate) {
		this.bookCopyId = bookCopyId;
		this.subscriberId = subscriberId;
		this.action = action;
		this.date = date;
		this.endDate = endDate;
	}

	public HistoryEntry(int subscriberId, int id, String action, LocalDateTime date) {
		this.subscriberId = subscriberId;
		this.id = id;
		this.action = action;
		this.date = date;
	}

	public HistoryEntry(int subscriberId, String action, int bookCopyId) {
		this.action = action;
		this.subscriberId = subscriberId;
		this.bookCopyId = bookCopyId;
		date = LocalDateTime.now();
	}

	public HistoryEntry(int subscriberId, String action) {
		this.action = action;
		this.subscriberId = subscriberId;
		date = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return id + " " + action + " " + date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public int getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	public int getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(int bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

	public String getBook() {
		return book;
	}

	public void setBook(String book) {
		this.book = book;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
}