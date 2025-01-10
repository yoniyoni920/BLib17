package entities;

import java.io.Serializable;
import java.time.LocalDate;

public class BookCopy implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private int copyId;
	private int bookId ;
	private LocalDate lendDate;
	private LocalDate returnDate;
	private int borrowerID;
	private int ordererID;
	private Book book ;

	public BookCopy(int copyId, int bookId , LocalDate lendDate , LocalDate returnDate , int borrowerID , int ordererID) {
		this.copyId = copyId;
		this.bookId = bookId;
		this.lendDate = lendDate ;
		this.returnDate = returnDate ;
		this.borrowerID = borrowerID;
        this.ordererID = ordererID;
    }

	public boolean isBorrowed(){
		return returnDate == null || returnDate.isBefore(LocalDate.now());
	}

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId ;
	}

	public int getCopyId() {
		return this.copyId;
	}

	public void setCopyId(int copyId) {
		this.copyId = copyId;
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

	public int getBorrowerId() {
		return this.borrowerID;
	}

	public void setBorrowerId(int subscriberID) {
		this.borrowerID = subscriberID ;
	}

	public void setBook(Book book) {
		this.book = book ;
	}

	public Book getBook() {
		return this.book ;
	}

	@Override
	public String toString() {
		return "copy " + copyId + " is borrowed from date " + lendDate + " to " + returnDate;
	}

    public int getOrdererID() {
        return ordererID;
    }

    public void setOrdererID(int ordererID) {
        this.ordererID = ordererID;
    }
}