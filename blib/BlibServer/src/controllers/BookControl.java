package controllers;

import entities.BookCopy;
import entities.BorrowReport;
import java.sql.*;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

					String locationOrDate = "jljl";
					try(PreparedStatement preparedStatement1 = DBControl.getConnection().prepareStatement(query1)){
						preparedStatement1.setInt(1, id);
						ResultSet rs1 = preparedStatement1.executeQuery();
						if(rs1.next())
							locationOrDate = "Shelf "+location;
						else {
							try(PreparedStatement preparedStatement2 = DBControl.getConnection().prepareStatement(query2)){
								preparedStatement2.setInt(1, id);
								ResultSet rs2 = preparedStatement2.executeQuery();
								if(rs2.next()) {
									LocalDate originalDate = rs2.getDate("return_date").toLocalDate();
									locationOrDate = originalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
									locationOrDate = "Available By: "+locationOrDate;
						        	}
							}	
						}
					}

					Book book = new Book(id, title, authors, genre, description, image, location);
					book.setLocationOrDate(locationOrDate);
					books.add(book);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	  return books;
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
            stt.setInt(3, bookCopy.getBorrowSubscriberId());
            stt.setInt(4, bookCopy.getId());
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

	/**
	 * Retrieves a list of borrowed books by a subscriber from the database.
	 * This method fetches book copy details, including the lending and return dates, as well as the subscriber's IDs,
	 * and then associates the book copy with the corresponding book details (e.g., title, authors, genre).
	 *
	 * @param subscriber The subscriber whose borrowed books are being retrieved.
	 * @return A list of borrowed `BookCopy` objects associated with the given subscriber.
	 */
	public static List<BookCopy> retrieveBorrowedBooks(int subscriberId) {
		ResultSet rs;

		// Retrieving borrowed books for a subscriber
		List<BookCopy> borrowedBooks = new ArrayList<>();
		try {
			PreparedStatement stmt = DBControl.getConnection().prepareStatement("SELECT * FROM book_copy WHERE borrow_subscriber_id = ?");
			stmt.setInt(1, subscriberId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				int copyId = rs.getInt(1);
				int bookId = rs.getInt(2);
				Date sqlDate = rs.getDate(3);
				LocalDate lendDate = sqlDate.toLocalDate();
				sqlDate = rs.getDate(4);
				LocalDate returnDate = sqlDate.toLocalDate();
				int borrowSubscriberId = rs.getInt(5);
				int orderSubscriberId = rs.getInt(6);
				borrowedBooks.add(new BookCopy(copyId, bookId, lendDate, returnDate, borrowSubscriberId, orderSubscriberId));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Retrieving the book details (covers, title, etc.)
		Connection con = DBControl.getConnection();
		Statement stmt1 = null;
		try {
			stmt1 = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (BookCopy copy : borrowedBooks) {
			int bookId = copy.getBookId();
			try {
				rs = stmt1.executeQuery("SELECT * FROM book WHERE id = '" + bookId + "'");
				if (rs.next()) {
					int id = rs.getInt(1);
					String title = rs.getString(2);
					String authors = rs.getString(3);
					String genre = rs.getString(4);
					String description = rs.getString(5);
					String image = rs.getString(6);
					String location = rs.getString("location");

					copy.setBook(new Book(id, title, authors, genre, description, image, location));
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return borrowedBooks;
	}

    public static boolean orderBook(BookCopy bookCopy) {
        try (PreparedStatement stt = DBControl.getConnection().prepareStatement(
                "UPDATE book_copy SET order_subscriber_id = ? WHERE id = ?")) {
            stt.setInt(1, bookCopy.getOrderSubscriberId());
            stt.setInt(2, bookCopy.getId());
            stt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
	public static void returnBook(int bookCopyId) {
		String query = "UPDATE book_copy SET borrow_subscriber_id = ?, lend_date = ?, return_date = ? WHERE id = ?";
		try(PreparedStatement preparedStatemen = DBControl.getConnection().prepareStatement(query)){
			preparedStatemen.setNull(1, Types.INTEGER);
			preparedStatemen.setNull(2, java.sql.Types.DATE);
			preparedStatemen.setNull(3, java.sql.Types.DATE);
			preparedStatemen.setInt(4, bookCopyId);
			preparedStatemen.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}