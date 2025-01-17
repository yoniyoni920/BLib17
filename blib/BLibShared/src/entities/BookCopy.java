package entities;

import java.io.Serializable;
import java.time.LocalDate;

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
	private LocalDate lendDate;
	private LocalDate returnDate;
	private int borrowSubscriberID;
	private int orderSubscriberID;
	private Book book ;

	public BookCopy(int id, int bookId , LocalDate lendDate , LocalDate returnDate , int borrowSubscriberId, int orderSubscriberId) {
		this.id = id ;
		this.bookId = bookId ;
		this.lendDate = lendDate ;
		this.returnDate = returnDate ; 
		this.borrowSubscriberID = borrowSubscriberId;
		this.orderSubscriberID = orderSubscriberId;
		
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
	
	public LocalDate getLendDate() {
		return this.lendDate;
	}
	
	public void setLendDate(LocalDate lendDate) {
		this.lendDate = lendDate;
	}
	
	public LocalDate getReturnDate() {
		return this.returnDate;
	}
	
	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate ;
	}
	
	public int getBorrowSubscriberId() {
		return this.borrowSubscriberID ;
	}
	
	public void setBorrowSubscriberId(int subscriberID) {
		this.borrowSubscriberID = subscriberID ;
	}
	
	public int getOrderSubscriberId() {
		return this.orderSubscriberID ;
	}
	
	public void setOrderSubscriberId(int subscriberID) {
		this.orderSubscriberID = subscriberID ;
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