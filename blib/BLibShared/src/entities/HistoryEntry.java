package entities;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *A class to hold each entry of the subscriber's history
 */
public class HistoryEntry implements Serializable {
	private int id;
	private HistoryAction action;
	private int subscriberId;
	private LocalDateTime date;

	// These are more optional, depending on the action done
	private Integer librarianUserId;
	private Integer bookCopyId;
	private Integer bookId;
	private LocalDateTime endDate;

	private String bookName;
	private String librarianName;

	public HistoryEntry(int subscriberId, HistoryAction action, int bookCopyId, LocalDateTime date, LocalDateTime endDate) {
		this(subscriberId, action, date, endDate);
		this.bookCopyId = bookCopyId;
	}

	public HistoryEntry(int subscriberId, HistoryAction action, LocalDateTime date, LocalDateTime endDate) {
		this.subscriberId = subscriberId;
		this.action = action;
		this.date = date;
		this.endDate = endDate;
	}

	public HistoryEntry(int subscriberId, int id, HistoryAction action, LocalDateTime date) {
		this.subscriberId = subscriberId;
		this.id = id;
		this.action = action;
		this.date = date;
	}

	public HistoryEntry(int subscriberId, HistoryAction action, int bookCopyId) {
		this.action = action;
		this.subscriberId = subscriberId;
		this.bookCopyId = bookCopyId;
		date = LocalDateTime.now();
	}

	public HistoryEntry(int subscriberId, HistoryAction action) {
		this.action = action;
		this.subscriberId = subscriberId;
		date = LocalDateTime.now();
	}

	@Override
	public String toString() {
		return id + " " + action + " " + date;
	}

	public HistoryAction getAction() {
		return action;
	}

	public void setAction(HistoryAction action) {
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

	public Integer getBookCopyId() {
		return bookCopyId;
	}

	public void setBookCopyId(int bookCopyId) {
		this.bookCopyId = bookCopyId;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getLibrarianName() {
		return librarianName;
	}

	public void setLibrarianName(String librarianName) {
		this.librarianName = librarianName;
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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Integer getLibrarianUserId() {
        return librarianUserId;
    }

    public void setLibrarianUserId(Integer librarianUserId) {
        this.librarianUserId = librarianUserId;
    }
}