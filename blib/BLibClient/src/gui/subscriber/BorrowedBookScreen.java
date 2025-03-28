package gui.subscriber;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import base.Action;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import services.ClientUtils;
import services.InterfaceUtils;

/**
 * Controller for the Borrowed Book Screen.
 * This class displays information about a borrowed book, including the book cover, title, borrow date,
 * return date, and whether the book is ordered by another subscriber. It also shows the number of days 
 * left before the book is due for return or indicates if the book is overdue.
 */
public class BorrowedBookScreen extends AbstractScreen {
    @FXML private Label borrowDate;
    
    @FXML private Label returnDate;
    
    @FXML private Label copyId;
    
    @FXML private Label daysLeft;
    
    @FXML private VBox borrowExtend;

    private BookCopy copy;
    
    private Subscriber subscriber;

    @FXML private ImageView bookImageView;

    @FXML private Label bookTitleLabel;
    @FXML private Label descLabel;
    @FXML private Label authorsLabel;
    @FXML private Label genreLabel;
    @FXML private Label locationLabel;

    @Override
    public void openScreen(Object... args) {
        onStart((BookCopy)args[0], (Subscriber)args[1]);
    }

    /**
     * Initializes the screen with the provided book copy information.
     * This method is called when the screen is loaded and the book copy data is passed in.
     *
     * @param copy The book copy that was borrowed and whose information will be displayed.
     * @param subscriber The subscriber who borrowed the book.
     */
    public void onStart(BookCopy copy , Subscriber subscriber) {
        loadData(copy , subscriber);
        renderData();
    }

    /**
     * Loads the book copy data into the `copy` object.
     * This method prepares the book copy data to be rendered on the screen.
     *
     * @param copy The book copy whose information will be loaded.
     * @param subscriber The subscriber who borrowed the book.
     */
    private void loadData(BookCopy copy , Subscriber subscriber) {
        this.copy = copy;
        this.subscriber = subscriber;
    }

    /**
     * Renders the book's details on the screen, including its cover image, title, borrow and return dates,
     * order status, copy ID, and the number of days left before the book is due for return.
     */
    private void renderData() {
        borrowExtend.setVisible(false);

        String url = getClass().getResource("/resources/book_covers/" + copy.getBook().getImage()).toExternalForm();
        Image image = new Image(url);
        bookImageView.setImage(image);

        bookTitleLabel.setText(copy.getBook().getTitle());
        descLabel.setText(copy.getBook().getDescription());
        authorsLabel.setText(copy.getBook().getAuthors());
        genreLabel.setText(copy.getBook().getGenre());
        locationLabel.setText(copy.getBook().getLocation());
        borrowDate.setText(InterfaceUtils.formatDate(copy.getLendDate()));
        returnDate.setText(InterfaceUtils.formatDate(copy.getReturnDate()));
        copyId.setText(copy.getId() + "");

        int daysBetween = (int) ChronoUnit.DAYS.between(LocalDate.now(), copy.getReturnDate());
        if (daysBetween < 0) {
            daysLeft.setText(String.format("(%d Days Late!)", Math.abs(daysBetween)));
        } else if (daysBetween == 0) {
            daysLeft.setText("(Today!)");
        } else {
            daysLeft.setText(String.format("(%d Days Left)", daysBetween));
        }

        // Display the extend button only if the book is not ordered and it's close to due date
        borrowExtend.setVisible(daysBetween <= 7 && !subscriber.isFrozen());
        borrowExtend.setManaged(borrowExtend.isVisible());
    }

    /**
     * Handles the action of pressing the extend button to extend the borrow time for the book.
     * It sends a request to the server to extend the borrow time and updates the UI accordingly.
     *
     * @param event The action event triggered by the user.
     */
    public void onPressingExtendButton(ActionEvent event) {
        // Send to server
        copy.setReturnDate(copy.getReturnDate().plusDays(14));
        Message msgFromServer = ClientUtils.sendMessage(new Message(Action.EXTEND_BORROW_TIME, copy));

        // Extending book borrow time
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (!msgFromServer.isError()) {
            // Changing data and showing message
            borrowExtend.setVisible(false);
            returnDate.setText(InterfaceUtils.formatDate(copy.getReturnDate()));
            int daysBetween = (int) ChronoUnit.DAYS.between(LocalDate.now(), copy.getReturnDate());
            daysLeft.setText(String.format("(%d Days Left)", daysBetween));
            alert.setHeaderText("Your Borrow Duration Successfully Extended");
            alert.showAndWait();
        } else { // Failed to extend
            copy.setReturnDate(copy.getReturnDate().minusDays(14));
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Extending Your Borrow Time Duration");
            alert.setContentText("Couldn't extend the borrow duration. It's possible there's an order waiting for the book.");
            alert.showAndWait();
        }
    }
}
