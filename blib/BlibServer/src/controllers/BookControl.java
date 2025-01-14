package controllers;

import entities.Book;
import entities.BookCopy;
import entities.Subscriber;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class BookControl {

    public static void searchBooks() {
        // TODO - implement BookControl.searchBooks
        throw new UnsupportedOperationException();
    }

    public static void lendBook() {
        // TODO - implement BookControl.lendBook
        throw new UnsupportedOperationException();
    }

    public static BookCopy checkBookLendable(int bookId) {
		try (PreparedStatement stt = DBControl.getInstance().selectQuery("book_copy", "book_id", bookId)) {
			ResultSet rs = stt.executeQuery();
			while (rs.next()) {
				int copyId = rs.getInt("id");
				Date returnDate = rs.getDate("return_date");
				int orderSubscriberId = rs.getInt("order_subscriber_id");
				if (orderSubscriberId == 0 && (returnDate == null || returnDate.toLocalDate().isBefore(LocalDate.now()))) {
					Date lendDate = rs.getDate("lend_date");
					int borrowerId = rs.getInt("borrow_subscriber_id");
					return new BookCopy(copyId, bookId, lendDate == null?null:lendDate.toLocalDate(),
                            returnDate == null? null:returnDate.toLocalDate(), borrowerId, orderSubscriberId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

    public static BookCopy checkBookOrderable(int bookId) {
        try (PreparedStatement stt = DBControl.getInstance().selectQuery("book_copy", "book_id", bookId)) {
            ResultSet rs = stt.executeQuery();
            BookCopy earliestCopy = null;
            while (rs.next()) {
                int copyId = rs.getInt("id");
                Date returnDate = rs.getDate("return_date");
                int orderSubscriberId = rs.getInt("order_subscriber_id");
                if (orderSubscriberId == 0) {
                    if (earliestCopy == null ||  returnDate.toLocalDate().isBefore(earliestCopy.getReturnDate())) {
                        Date lendDate = rs.getDate("lend_date");
                        int borrowerSubscriberId = rs.getInt("borrow_subscriber_id");
                       earliestCopy = new BookCopy(copyId, bookId, lendDate == null?null:lendDate.toLocalDate(),
                               returnDate == null ? null : returnDate.toLocalDate(),borrowerSubscriberId, orderSubscriberId );
                    }
                }
            }
            return earliestCopy;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean lendBookToSubscriber(BookCopy bookCopy) {
        try (PreparedStatement stt = DBControl.getConnection().prepareStatement(
                "UPDATE book_copy SET lend_date = ?, return_date = ?, borrow_subscriber_id = ? WHERE id = ?")) {
            stt.setDate(1, Date.valueOf(bookCopy.getLendDate()));
            stt.setDate(2, Date.valueOf(bookCopy.getReturnDate()));
            stt.setInt(3, bookCopy.getBorrowerId());
            stt.setInt(4, bookCopy.getCopyId());
            stt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Book searchBookById(int bookId) {
        try (PreparedStatement stt = DBControl.getInstance().selectQuery("book", "id", bookId)) {
            ResultSet rs = stt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("authors");
                String genre = rs.getString("genre");
                String description = rs.getString("description");
                String image = rs.getString("image");
                String location = rs.getString("location");
                return new Book(id, title, author, genre, description, image, location);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void allBookCopiesLoaned() {
        // TODO - implement BookControl.allBookCopiesLoaned
        throw new UnsupportedOperationException();
    }

    public static void orderBook() {
        // TODO - implement BookControl.orderBook
        throw new UnsupportedOperationException();
    }

    public static void bookOrderExists() {
        // TODO - implement BookControl.bookOrderExists
        throw new UnsupportedOperationException();
    }

    public static void sendMessageToLibrarian() {
        // TODO - implement BookControl.sendMessageToLibrarian
        throw new UnsupportedOperationException();
    }

    public static void returnBook() {
        // TODO - implement BookControl.returnBook
        throw new UnsupportedOperationException();
    }

    public static void updateBookCopyReturnDate() {
        // TODO - implement BookControl.updateBookCopyReturnDate
        throw new UnsupportedOperationException();
    }

    public static void generateBorrowBookTimes() {
        // TODO - implement BookControl.generateBorrowBookTimes
        throw new UnsupportedOperationException();
    }

    public static void searchBookByBarcode() {
        // TODO - implement BookControl.searchBookByBarcode
        throw new UnsupportedOperationException();
    }

}