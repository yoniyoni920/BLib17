package entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * This class represents a Book Copy in the library system.
 * It contains the borrow information , what book is it...
 *
 * @author Helal Hammoud
 * @version 1.0
 * @since 2025-01-08
 */
public class BookCopy implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private int bookId ;
	private LocalDateTime lendDate;
	private LocalDateTime returnDate;
	private int borrowSubscriberID;
	private Book book ;

	public BookCopy(int id, int bookId , LocalDateTime lendDate , LocalDateTime returnDate , int borrowSubscriberId) {
		this.id = id ;
		this.bookId = bookId ;
		this.lendDate = lendDate ;
		this.returnDate = returnDate ; 
		this.borrowSubscriberID = borrowSubscriberId;
	}
	
	public int getBookId() {
		return bookId;
	}
	
	public void setBookId(int bookId) {
		this.bookId = bookId ;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setCopyId(int copyId) {
		this.id = copyId;
	}
	
	public LocalDateTime getLendDate() {
		return this.lendDate;
	}
	
	public void setLendDate(LocalDateTime lendDate) {
		this.lendDate = lendDate;
	}
	
	public LocalDateTime getReturnDate() {
		return this.returnDate;
	}
	
	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate ;
	}
	
	public int getBorrowSubscriberId() {
		return this.borrowSubscriberID ;
	}
	
	public void setBorrowSubscriberId(int subscriberID) {
		this.borrowSubscriberID = subscriberID ;
	}
	
	public void setBook(Book book) {
		this.book = book ;
	}
	
	public Book getBook() {
		return this.book ;
	}
 	
	@Override
	public String toString() {
		return "copy " + id + " is borrowed from date " + lendDate + " to " + returnDate;
	}
	
}