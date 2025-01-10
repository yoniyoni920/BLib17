package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;
import java.time.LocalDate;

import entities.Book;
import entities.BookCopy;
import entities.Subscriber;

/**
 * Controller for managing operations related to book copies.
 * This class handles the retrieval of borrowed books by a subscriber, along with associated book details.
 * It communicates with the database to fetch the relevant information about the borrowed books and their metadata.
 * 
 * @author Helal Hammoud
 */
public class BookCopyControl {

    /**
     * Retrieves a list of borrowed books by a subscriber from the database.
     * This method fetches book copy details, including the lending and return dates, as well as the subscriber's IDs,
     * and then associates the book copy with the corresponding book details (e.g., title, authors, genre).
     *
     * @param subscriber The subscriber whose borrowed books are being retrieved.
     * @return A list of borrowed `BookCopy` objects associated with the given subscriber.
     */
    public static List<BookCopy> retrieveBorrowedBooks(Subscriber subscriber) {
        ResultSet rs;

        // Retrieving borrowed books for a subscriber
        List<BookCopy> borrowedBooks = new ArrayList<>();
        try {
            PreparedStatement stmt = DBControl.getConnection().prepareStatement("SELECT * FROM book_copy WHERE borrow_subscriber_id = ?");
            stmt.setString(1, subscriber.getId().toString());
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
        System.out.println(borrowedBooks);
        return borrowedBooks;
    }
}
