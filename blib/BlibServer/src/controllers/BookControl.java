package controllers;

import entities.BorrowReport;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import entities.Book;

public class BookControl {

	public static List<Book> searchBooks(String search, String searchType) {
		List<Book> books = new ArrayList<>();
		if(searchType.equals("title") || searchType.equals("genre") || searchType.equals("description")) {
			String query = "SELECT * FROM book WHERE " + searchType + " LIKE ?";
			String query1 = "SELECT * FROM book_copy\r\n"
					+ "WHERE book_id = ? AND borrow_subscriber_id IS NULL AND order_subscriber_id IS NULL;";
			String query2 = "SELECT *\r\n" + "FROM book_copy\r\n"
					+ "WHERE book_id = ? \r\n"+ "ORDER BY ABS(DATEDIFF(return_date, CURDATE())) ASC\r\n"
					+ "LIMIT 1;";
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
				}

					String locationOrDate = "jljl";
					try(PreparedStatement preparedStatement1 = DBControl.getConnection().prepareStatement(query1)){
						preparedStatement1.setInt(1, id);
						ResultSet rs1 = preparedStatement1.executeQuery();
						if(rs1.next())
							locationOrDate = "Located on Shelf "+location+".";
						else {
							try(PreparedStatement preparedStatement2 = DBControl.getConnection().prepareStatement(query2)){
								preparedStatement2.setInt(1, id);
								ResultSet rs2 = preparedStatement2.executeQuery();
								if(rs2.next()) {
									LocalDate originalDate = rs2.getDate("return_date").toLocalDate();
									locationOrDate = originalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
									locationOrDate = "Not available, closest return date: "+locationOrDate+".";
						        	} 
							}	
						}
					}
					 
					books.add(new Book(id, title, authors, genre, description, image, location, locationOrDate));
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
			query = "SELECT *, book.* FROM borrow_report INNER JOIN book ON book.id = borrow_report.book_id " +
					"WHERE report_date = ?";
		} else {
			query = "SELECT *, book.* FROM borrow_report INNER JOIN book ON book.id = borrow_report.book_id " +
					"WHERE report_date = ? AND book_id = ?";
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
				BorrowReport report = new BorrowReport(
					rs.getInt("book_id"),
					rs.getInt("book_copy_id"),
					rs.getDate("start_date").toLocalDate(),
					rs.getDate("return_date").toLocalDate(),
					lateDate != null ? lateDate.toLocalDate() : null
				);
				report.setBook(new Book(
					rs.getInt("book_id"),
					rs.getString("title"),
					rs.getString("authors"),
					rs.getString("genre"),
					rs.getString("description"),
					rs.getString("image"),
					rs.getString("location")
				));
				list.add(report);
			}

			return list;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}