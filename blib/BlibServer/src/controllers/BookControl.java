package controllers;

import entities.Book;
import entities.BorrowReport;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookControl {

	public static List<Book> searchBooks(String search, String searchType){
		List<Book> books = new ArrayList<>();
		if(searchType.equals("title") ||searchType.equals("genre") || searchType.equals("description")) {
			String query = "SELECT * FROM book WHERE " + searchType + " LIKE ?";
			try (PreparedStatement preparedStatement = DBControl.getConnection().prepareStatement(query)) {
				preparedStatement.setString(1, "%"+search+"%");
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					int id = rs.getInt("id");
					String title = rs.getString("title");
					String authors = rs.getString("authors");
					String genre = rs.getString("genre");
					String description = rs.getString("description");
					String image = rs.getString("image");
					String location = rs.getString("location");
					books.add(new Book(id, title, authors, genre, description, image, location));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return books;
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

	public static void searchBookById() {
		// TODO - implement BookControl.searchBookById
		throw new UnsupportedOperationException();
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

	/**
	 * Returns borrow times report for a specific book
	 * @param bookId
	 */
	public static List<BorrowReport> getBorrowTimesReport(LocalDate date, Integer bookId) {
		String query;
		if (bookId == null) {
			query = "SELECT * FROM borrow_report WHERE report_date = ?";
		} else {
			query = "SELECT * FROM borrow_report WHERE report_date = ? AND book_id = ?";
		}

		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			st.setObject(1, date);

			if (bookId != null) {
				st.setInt(2, bookId);
			}

			ResultSet rs = st.executeQuery();
			List<BorrowReport> list = new ArrayList<>();
			while (rs.next()) {
				Date lateDate = rs.getDate("late_return_date");
				list.add(new BorrowReport(
					rs.getInt("book_id"),
					rs.getInt("book_copy_id"),
					rs.getDate("start_date").toLocalDate(),
					rs.getDate("return_date").toLocalDate(),
					lateDate != null ? lateDate.toLocalDate() : null
				));
			}

			return list;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}