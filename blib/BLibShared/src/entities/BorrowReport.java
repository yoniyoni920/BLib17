package entities;

import java.io.Serializable;
import java.time.LocalDate;

public class BorrowReport implements Serializable {
    private int bookId;
    private int bookCopyId;
    private Book book;
    private LocalDate startDate;
    private LocalDate returnDate;
    private LocalDate lateReturnDate;

    private boolean isLate;

    public BorrowReport(int bookId, int bookCopyId, LocalDate startDate, LocalDate returnDate,
                        boolean isLate, LocalDate lateReturnDate) {
        this.bookId = bookId;
        this.bookCopyId = bookCopyId;
        this.startDate = startDate;
        this.returnDate = returnDate;
        this.isLate = isLate;
        this.lateReturnDate = lateReturnDate;
    }

    public int getBookId() {
        return bookId;
    }

    public int getBookCopyId() {
        return bookCopyId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LocalDate getLateReturnDate() {
        return lateReturnDate;
    }

    public boolean isLate() {
        return isLate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
