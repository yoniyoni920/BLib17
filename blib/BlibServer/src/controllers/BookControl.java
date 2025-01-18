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
/**
 * The BookControl class provides various methods for managing books and their copies,
 * including search, lending, ordering, marking as lost, and generating reports.
 */
public class BookControl {
	/**
     * Searches for books based on the specified search term and search type.
     *
     * @param search The search term.
     * @param searchType The type of search (e.g., "title", "genre", "description").
     * @return A list of books matching the search criteria.
     */
	public static List<Book> searchBooks(String search, String searchType) {
		List<Book> books = new ArrayList<>();
		if(searchType.equals("title") || searchType.equals("genre") || searchType.equals("description")) {
			String query = "SELECT * FROM book WHERE " + searchType + " LIKE ?";

			// Query tries to find at lesat one copy that isn't borrowed or ordered.
			String queryAvailable = "SELECT 1 FROM book_copy "
					+ "WHERE book_id = ? AND borrow_subscriber_id IS NULL AND order_subscriber_id IS NULL LIMIT 1";

			// Query selects one copy that is not being ordered and has the closest return date
			String queryOrderable = "SELECT * FROM book_copy "
					+ "WHERE book_id = ? AND return_date IS NOT NULL AND order_subscriber_id IS NULL " +
					"ORDER BY ABS(DATEDIFF(return_date, CURDATE())) ASC LIMIT 1";

			try (PreparedStatement ps = DBControl.prepareStatement(query)) {
				ps.setString(1, "%"+search+"%");
				ResultSet bookResult = ps.executeQuery();

				while (bookResult.next()) {
					int id = bookResult.getInt("id");
					String title = bookResult.getString("title");
					String authors = bookResult.getString("authors");
					String genre = bookResult.getString("genre");
					String description = bookResult.getString("description");
					String image = bookResult.getString("image");
					String location = bookResult.getString("location");

					String locationOrDate;
					try(PreparedStatement ps2 = DBControl.prepareStatement(queryAvailable)){
						ps2.setInt(1, id);
						if (ps2.executeQuery().next())
							locationOrDate = "Shelf "+location;
						else {
							try(PreparedStatement ps3 = DBControl.prepareStatement(queryOrderable)){
								ps3.setInt(1, id);
								ResultSet orderResult = ps3.executeQuery();
								if(orderResult.next()) {
									LocalDate originalDate = orderResult.getDate("return_date").toLocalDate();
									locationOrDate = originalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
									locationOrDate = "Available By: "+locationOrDate;
								} else {
									locationOrDate = "Unavailable";
								}
							}	
						}
					}

					Book book = new Book(id, title, authors, genre, description, image, location);
					book.setLocationOrDate(locationOrDate);
					books.add(book);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	  return books;
	}
	/**
     * Checks if a book copy is lendable based on its availability and return date.
     *
     * @param bookId The ID of the book to check.
     * @return A lendable BookCopy, or null if none are available.
     */
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
	
/**
 * Checks if a book is orderable and retrieves the earliest available copy.
 *
 * @param bookId The ID of the book to check.
 * @return An orderable BookCopy, or null if none are available.
 */
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

    /**
     * Lends a book to a subscriber by updating its database record.
     *
     * @param bookCopy The BookCopy to lend.
     * @return True if the operation was successful, false otherwise.
     */
    public static boolean lendBookToSubscriber(BookCopy bookCopy) {
        try (PreparedStatement stt = DBControl.getConnection().prepareStatement(
                "UPDATE book_copy SET lend_date = ?, return_date = ?, borrow_subscriber_id = ? WHERE id = ?")) {
            stt.setDate(1, Date.valueOf(bookCopy.getLendDate()));
            stt.setDate(2, Date.valueOf(bookCopy.getReturnDate()));
            stt.setInt(3, bookCopy.getBorrowSubscriberId());
            stt.setInt(4, bookCopy.getCopyId());
            stt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Searches for a book by its unique identifier (ID).
     *
     * @param bookId The ID of the book to search for.
     * @return A Book object containing the details of the book, or null if not found.
     */
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
            stt.setInt(2, bookCopy.getCopyId());
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

	/**
	 * Marks a book as lost. This also automatically freezes the subscriber and makes a history point about that
	 * @param bookCopyId
	 */
	public static boolean markBookCopyAsLost(int bookCopyId) {
		String query = "SELECT * FROM book_copy WHERE id = ? AND is_lost = 0 LIMIT 1";
		try (PreparedStatement st = DBControl.prepareStatement(query)) {
			st.setInt(1, bookCopyId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				String upQuery = "UPDATE book_copy " +
						"SET is_lost = 1, borrow_subscriber_id = null, order_subscriber_id = null, lend_date = NULL, return_date = NULL " +
						"WHERE id = ?";
				try (PreparedStatement st2 = DBControl.prepareStatement(upQuery)) {
					st2.setInt(1, bookCopyId);
					//TODO: log history

					// Punish subscriber for losing the book
					SubscriberControl.freezeSubscriber(rs.getInt("borrow_subscriber_id"));
					return st2.executeUpdate() == 1;
				}
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return false;
	}

	public static boolean extendBorrowTime(BookCopy copy) {
		int changed = 0 ;
		try {
			PreparedStatement stmt = DBControl.getConnection().prepareStatement("UPDATE book_copy SET return_date = ? WHERE id = ?");
			stmt.setString(1,copy.getReturnDate().toString());
			stmt.setString(2, copy.getCopyId()+"");
			changed = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false ;
		}
		if(changed == 1)
			return true ;
		else
			return false ;
	}
}