package controllers;

import entities.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BookControl {

	public static void searchBooks() {
		// TODO - implement BookControl.searchBooks
		throw new UnsupportedOperationException();
	}

	public static void lendBook() {
		// TODO - implement BookControl.lendBook
		throw new UnsupportedOperationException();
	}

	private static void checkBookLendable() {
		// TODO - implement BookControl.checkBookLendable
		throw new UnsupportedOperationException();
	}

	public static void lendBookToSubscriber() {
		// TODO - implement BookControl.lendBookToSubscriber
		throw new UnsupportedOperationException();
	}

	public static Book searchBookById(int bookId) {
		try(PreparedStatement stt = DBControl.getInstance().selectQuery("book", "id", bookId)){
			ResultSet rs = stt.executeQuery();
			if(rs.next()){
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String author = rs.getString("authors");
				String genre = rs.getString("genre");
				String description = rs.getString("description");
				String image = rs.getString("image");
				String location = rs.getString("location");
                return new Book(id, title, author, genre, description, image, location);
			}

		}catch(Exception e) {
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