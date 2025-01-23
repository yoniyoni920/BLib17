package controllers;

import entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The BookControl class provides various methods for managing books and their copies,
 * including search, lending, ordering, marking as lost, and generating reports.
 */
public class BookControl {
    /**
     * Searches for books based on the specified search term and search type.
     *
     * @param search     The search term.
     * @param searchType The type of search (e.g., "title", "genre", "description").
     * @return A list of books matching the search criteria.
     */
    public static List<Book> searchBooks(String search, String searchType) {
        List<Book> books = new ArrayList<>();
        if (searchType.equals("title") || searchType.equals("genre") || searchType.equals("description")) {
            String query = "SELECT * FROM book WHERE " + searchType + " LIKE ?";

            // Query tries to find at lesat one copy that isn't borrowed or ordered.
            String queryAvailable = "SELECT 1 FROM book_copy "
                    + "WHERE book_id = ? AND borrow_subscriber_id IS NULL AND order_subscriber_id IS NULL LIMIT 1";

            // Query selects one copy that is not being ordered and has the closest return date
            String queryOrderable = "SELECT * FROM book_copy "
                    + "WHERE book_id = ? AND return_date IS NOT NULL AND order_subscriber_id IS NULL " +
                    "ORDER BY ABS(DATEDIFF(return_date, CURDATE())) ASC LIMIT 1";

            try (PreparedStatement ps = DBControl.prepareStatement(query)) {
                ps.setString(1, "%" + search + "%");
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
                    try (PreparedStatement ps2 = DBControl.prepareStatement(queryAvailable)) {
                        ps2.setInt(1, id);
                        if (ps2.executeQuery().next())
                            locationOrDate = "Shelf " + location;
                        else {
                            try (PreparedStatement ps3 = DBControl.prepareStatement(queryOrderable)) {
                                ps3.setInt(1, id);
                                ResultSet orderResult = ps3.executeQuery();
                                if (orderResult.next()) {
                                    LocalDate originalDate = orderResult.getDate("return_date").toLocalDate();
                                    locationOrDate = originalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                    locationOrDate = "Available By: " + locationOrDate;
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return books;
    }

    /**
     * Checks if a book copy is lendable based on its availability and return date.
     *
     * @param bookId       The ID of the book to check.
     * @param subscriberId The ID of the subscriber to handle ordered books
     * @return The copy ID of the available copy.
     */
    public static int checkBookLendable(int bookId, int subscriberId) {
        //check if ordered
        if (subscriberId != 0) {
            try (PreparedStatement stt = DBControl.getInstance().selectQuery("book_order",
                    "book_id", bookId, "subscriber_id", subscriberId);) {
                ResultSet rs = stt.executeQuery();
                if (rs.next()) {
                    try (PreparedStatement stt1 = DBControl.getConnection().prepareStatement("SELECT id, return_date FROM " +
                            "book_copy where book_id = ? ORDER BY return_date")) {
                        stt1.setInt(1, bookId);
                        ResultSet rs1 = stt1.executeQuery();
                        if (rs1.next()) {
                            if (rs1.getDate("return_date") == null) {
                                return rs1.getInt("id");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try (PreparedStatement stt = DBControl.getConnection().prepareStatement(
                "select (select count(book_order.book_id) from book_order where book_id = ?) as order_count," +
                        " (select count(book_copy.book_id) from book_copy where book_id = ? and " +
                        "return_date IS NULL) as available_copies," +
                        "    (select book_copy.id from book_copy where book_id = ?" +
                        " order by return_date limit 1) as available_copy_id")) {
            stt.setInt(1, bookId);
            stt.setInt(2, bookId);
            stt.setInt(3, bookId);
            ResultSet rs = stt.executeQuery();
            if (rs.next()) {
                int copyId = rs.getInt("available_copy_id");
                int orderCount = rs.getInt("order_count");
                int availableCopies = rs.getInt("available_copies");
                if (availableCopies > orderCount) {
                    return copyId;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Checks if a book is orderable and retrieves the earliest available copy.
     *
     * @param bookId The ID of the book to check.
     * @return LocalDate representing the date book with be available or null if not orderable.
     */
    public static LocalDate checkBookOrderable(int bookId) {
        try (PreparedStatement stt = DBControl.getConnection().prepareStatement(
                "select (select count(book_order.book_id) from book_order where book_id = ?) as order_count," +
                        " (select count(book_copy.book_id) from book_copy where book_id = ?) as copy_count")) {
            stt.setInt(1, bookId);
            stt.setInt(2, bookId);
            ResultSet rs = stt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("order_count") < rs.getInt("copy_count")) {
                    try (PreparedStatement stt2 = DBControl.getConnection().prepareStatement(
                            "select book_copy.return_date from book_copy where book_id = ? order by return_date limit 1 offset ?")) {
                        stt2.setInt(1, bookId);
                        stt2.setInt(2, rs.getInt("order_count"));
                        ResultSet rs2 = stt2.executeQuery();
                        if (rs2.next()) {
                            return rs2.getDate("return_date").toLocalDate();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
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

            int subscriberId = bookCopy.getBorrowSubscriberId();
            int bookCopyId = bookCopy.getId();
            LocalDateTime date = bookCopy.getLendDate();
            LocalDateTime returnDate = bookCopy.getReturnDate();

            stt.setDate(1, Date.valueOf(date.toLocalDate()));
            stt.setDate(2, Date.valueOf(returnDate.toLocalDate()));
            stt.setInt(3, subscriberId);
            stt.setInt(4, bookCopyId);
            stt.executeUpdate();

            SubscriberControl.logIntoHistory(new HistoryEntry(
                subscriberId,
                HistoryAction.BORROW_BOOK,
                bookCopyId,
                date,
                returnDate
            ));
            
            try (PreparedStatement stt2 = DBControl.getConnection().prepareStatement(
                    "delete from book_order where book_id = ? and subscriber_id = ?")) {
                stt2.setInt(1, bookCopy.getBookId());
                stt2.setInt(2, bookCopy.getBorrowSubscriberId());
                stt2.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
     * @param subscriberId The subscriber whose borrowed books are being retrieved.
     * @return A list of borrowed `BookCopy` objects associated with the given subscriber.
     */
    public static List<BookCopy> retrieveBorrowedBooks(int subscriberId) {
        List<BookCopy> borrowedBooks = new ArrayList<>();
        String query = "SELECT * FROM book_copy " +
                "JOIN book ON book.id = book_copy.book_id " +
                "WHERE borrow_subscriber_id = ?";
        try (PreparedStatement ps = DBControl.prepareStatement(query)) {
            ps.setInt(1, subscriberId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Copy data
                int copyId = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int borrowSubscriberId = rs.getInt("borrow_subscriber_id");
                LocalDateTime lendDate = rs.getTimestamp("lend_date").toLocalDateTime();
                LocalDateTime returnDate = rs.getTimestamp("return_date").toLocalDateTime();
                BookCopy copy = new BookCopy(copyId, bookId, lendDate, returnDate, borrowSubscriberId);

                // Book data
                String title = rs.getString("title");
                String authors = rs.getString("authors");
                String genre = rs.getString("genre");
                String description = rs.getString("description");
                String image = rs.getString("image");
                String location = rs.getString("location");
                copy.setBook(new Book(bookId, title, authors, genre, description, image, location));

                borrowedBooks.add(copy);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowedBooks;
    }

    /**
     * Orders a book
     *
     * @param bookOrder object representing the book order
     * @return boolean if successful
     */
    public static boolean orderBook(BookOrder bookOrder) {
        try (PreparedStatement stt = DBControl.prepareStatement(
                "INSERT INTO book_order(subscriber_id, book_id) VALUES (?, ?)")) {

            int subscriberId = bookOrder.getSubscriberId();
            int bookId = bookOrder.getBookId();

            stt.setInt(1, subscriberId);
            stt.setInt(2, bookId);
            stt.executeUpdate();

            HistoryEntry entry = new HistoryEntry(subscriberId, HistoryAction.ORDER_BOOK);
            entry.setBookId(bookId);
            SubscriberControl.logIntoHistory(entry);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns borrow times report for a specific book
     *
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
                    rs.getBoolean("is_late"),
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateSubOrderReady(int bookId){
        try(PreparedStatement sttm2 = DBControl.prepareStatement(
                "select email, first_name, last_name, title from " +
                        "(subscriber join user on subscriber.user_id = user.id)" +
                        " join (book_order join book on book_order.book_id = book.id)" +
                        " on subscriber_id = subscriber.id where ordered_until = CURDATE() + 2 AND book_id = ?")){
            sttm2.setInt(1, bookId);
            ResultSet rs = sttm2.executeQuery();
            if (rs.next()) {
                CommunicationManager.sendMail(rs.getString("email"),
                        rs.getString("title") + " Order", "Hi "
                                + rs.getString("first_name") + " " + rs.getString("last_name")
                        +",<br>Ordered book '" + rs.getString("title") + "' is ready for pickup.<br>" +
                                "If the book isn't picked up in 2 days, the order will be canceled automatically.<br>" +
                                "Blib Library.","Blib Orders");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReturnForOrders(int bookCopyId) {
        try (PreparedStatement stt = DBControl.prepareStatement("SELECT book_id from book_copy WHERE id = ?")){
            stt.setInt(1, bookCopyId);
            ResultSet rs = stt.executeQuery();
            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                String updateQuery = "UPDATE book_order SET ordered_until = CURDATE() + 2 WHERE book_id = ? " +
                        "AND ordered_until IS NULL order by date limit 1";

                try (PreparedStatement sttm = DBControl.prepareStatement(updateQuery)) {
                    sttm.setInt(1, bookId);
                    sttm.execute();
                    updateSubOrderReady(bookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void returnBook(int bookCopyId) {
        try (PreparedStatement ps = DBControl.prepareStatement("SELECT * FROM book_copy WHERE id = ?")) {
            ps.setInt(1, bookCopyId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String updateQuery = "UPDATE book_copy SET borrow_subscriber_id = ?, lend_date = ?, return_date = ? WHERE id = ?";
                int subscriberId = rs.getInt("borrow_subscriber_id");
                try (PreparedStatement ps2 = DBControl.prepareStatement(updateQuery)) {
                    ps2.setNull(1, Types.INTEGER);
                    ps2.setNull(2, java.sql.Types.DATE);
                    ps2.setNull(3, java.sql.Types.DATE);
                    ps2.setInt(4, bookCopyId);
                    ps2.executeUpdate();

                    SubscriberControl.logIntoHistory(
                        new HistoryEntry(subscriberId, HistoryAction.RETURN_BOOK, bookCopyId)
                    );

                    // Attempts to update the late entry to include an actual return date
                    String updateLateQuery = "UPDATE subscriber_history SET end_date = now() " +
                            "WHERE book_copy_id = ? AND subscriber_id = ? " +
                            "AND action = 'late' AND end_date IS NULL";
                    try (PreparedStatement ps3 = DBControl.prepareStatement(updateLateQuery)) {
                        ps3.setInt(1, bookCopyId);
                        ps3.setInt(2, subscriberId);
                        ps3.executeUpdate();
                    }

                    updateReturnForOrders(bookCopyId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Marks a book as lost. This also automatically freezes the subscriber and makes a history point about that
     *
     * @param bookCopyId
     */
    public static boolean markBookCopyAsLost(int bookCopyId) {
        String query = "SELECT * FROM book_copy WHERE id = ? AND is_lost = 0 LIMIT 1";
        try (PreparedStatement st = DBControl.prepareStatement(query)) {
            st.setInt(1, bookCopyId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String upQuery = "UPDATE book_copy " +
                        "SET is_lost = 1, is_late = 0, borrow_subscriber_id = null, " +
                        "order_subscriber_id = null, lend_date = NULL, return_date = NULL " +
                        "WHERE id = ?";

                int subscriberId = rs.getInt("borrow_subscriber_id");
                try (PreparedStatement st2 = DBControl.prepareStatement(upQuery)) {
                    st2.setInt(1, bookCopyId);
                    // Log this into the subscriber's history
                    SubscriberControl.logIntoHistory(
                        new HistoryEntry(subscriberId, HistoryAction.LOST_BOOK, bookCopyId)
                    );
                    // Punish subscriber for losing the book
                    SubscriberControl.freezeSubscriber(subscriberId);
                    return st2.executeUpdate() == 1;
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return false;
    }

    /**
     * Extends the borrowing duration of the book
     * @param copy
     * @return whether the extension has succeeded
     */
    public static boolean extendBorrowTime(BookCopy copy, User userRequesting) {
        int id = copy.getId();
        int bookId = copy.getBookId();

        boolean isLibrarian = userRequesting.getRole() == Role.LIBRARIAN;

        String checkQuery = "SELECT 1 FROM book_order WHERE book_id = ?";
        // Don't allow extending if the book is being ordered (unless the librarian is asking)
        if (!isLibrarian) {
            try (PreparedStatement ps = DBControl.prepareStatement(checkQuery)) {
                ps.setInt(1, bookId);
                if (ps.executeQuery().next()) {
                    return false;
                }
            }  catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        String query = "UPDATE book_copy SET return_date = ? WHERE id = ?";
        try (PreparedStatement stmt = DBControl.prepareStatement(query)) {
            stmt.setString(1, copy.getReturnDate().toString());
            stmt.setString(2, id + "");

            int subId = copy.getBorrowSubscriberId();

            // Add history entry
            HistoryEntry entry;
            if (userRequesting.getRole() == Role.LIBRARIAN) {
                entry = new HistoryEntry(subId, HistoryAction.EXTEND_BORROW_LIBRARIAN, id);
                entry.setLibrarianUserId(userRequesting.getId());
            } else {
                entry = new HistoryEntry(subId, HistoryAction.EXTEND_BORROW_SUBSCRIBER, id);

                NotificationControl.saveNotification(
                    new Notification(
                        subId,
                        String.format("Extended Borrow Duration for Book: %s (Copy %d) by 14 Days", copy.getBook().getTitle(), copy.getId())
                    )
                );
            }
            entry.setEndDate(copy.getReturnDate());
            SubscriberControl.logIntoHistory(entry);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This functions returns records of the books that needs to be returned tomorrow
     *
     * @return Array list of map that contains name, phone_number, email, title
     */
    public static ArrayList<Map<String, Object>> getBooksForReturnReminder() {
        ArrayList<Map<String, Object>> records = new ArrayList<>();
        String query = "select book.title, subscriber.email, subscriber.phone_number, user.first_name, last_name" +
                " from (((book_copy join book on book_copy.book_id = book.id)" +
                " join subscriber on borrow_subscriber_id = subscriber.id)" +
                " join user on user_id = user.id) where return_date = ?";

        try (PreparedStatement stmt = DBControl.prepareStatement(query)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now().plusDays(1)));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("title", rs.getString("book.title"));
                record.put("email", rs.getString("subscriber.email"));
                record.put("phone_number", rs.getString("subscriber.phone_number"));
                record.put("name", rs.getString("user.first_name") + " " + rs.getString("user.last_name"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}