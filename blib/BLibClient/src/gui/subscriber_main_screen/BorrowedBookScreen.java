package gui.subscriber_main_screen;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import entities.BookCopy;
import gui.AbstractScreen;
import gui.BookCard;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the Borrowed Book Screen.
 * This class displays information about a borrowed book, including the book cover, title, borrow date,
 * return date, and whether the book is ordered by another subscriber. It also shows the number of days 
 * left before the book is due for return or indicates if the book is overdue.
 * 
 * @author Helal Hammoud
 */
public class BorrowedBookScreen extends AbstractScreen {

    @FXML
    private Label welcomeText;

    @FXML
    private Label borrowDate;
    
    @FXML
    private Label returnDate;
    
    @FXML
    private Label copyId;
    
    @FXML
    private Label daysLeft;
    
    @FXML
    private VBox borrowExtend;

    private BookCopy copy;

    @FXML
    private BookCard bookCardController;

    /**
     * Initializes the screen with the provided book copy information.
     * This method is called when the screen is loaded and the book copy data is passed in.
     *
     * @param copy The book copy that was borrowed and whose information will be displayed.
     */
    public void onStart(BookCopy copy) {
        fadeInLabelTransition(welcomeText);
        loadData(copy);
        renderData();
    }

    /**
     * Loads the book copy data into the `copy` object.
     * This method prepares the book copy data to be rendered on the screen.
     *
     * @param copy The book copy whose information will be loaded.
     */
    private void loadData(BookCopy copy) {
        this.copy = copy;
    }

    /**
     * Renders the book's details on the screen, including its cover image, title, borrow and return dates,
     * order status, copy ID, and the number of days left before the book is due for return.
     */
    private void renderData() {
        bookCardController.setBookData(copy.getBook());

        borrowDate.setText(copy.getLendDate().toString());
        returnDate.setText(copy.getReturnDate().toString());
//        isOrdered.setText("no");
        copyId.setText(copy.getCopyId() + "");
        
        int daysBetween = (int) ChronoUnit.DAYS.between(LocalDate.now(), copy.getReturnDate());
        if (daysBetween < 14) {
            daysLeft.setText(daysBetween + " Days Left");
        } else {
            daysLeft.setText("You Are Late");
        }

        if (copy.getOrderSubscriberId() == 0 && daysBetween <= 7) {
            borrowExtend.setVisible(true);
        } else {
//            isOrdered.setText("yes");
        }
    }
    
    public void openExtendBorrowTimeScreen(ActionEvent event) throws IOException {
    	ExtendBorrowTimeScreen screen = (ExtendBorrowTimeScreen)screenManager.openScreen("subscriber_main_screen/ExtendBorrowTimeScreen", "Extension Screen");
    	screen.onStart(copy);
    }

    /**
     * Closes the current screen and returns to the previous screen.
     *
     * @param event The action event triggered by the close window action.
     * @throws Exception If an error occurs while closing the screen.
     */
    public void closeWindow(ActionEvent event) throws Exception {
        screenManager.closeScreen();
    }

    /**
     * Performs a fade-in animation on the welcome label.
     * This method animates the opacity of the welcome text from 0 (invisible) to 1 (fully visible).
     *
     * @param welcomeText The label to apply the fade-in transition on.
     */
    private void fadeInLabelTransition(Label welcomeText) {
        welcomeText.setOpacity(0.0); // Start with the text invisible

        // First Fade-In Transition (Welcome Message)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeIn.setFromValue(0.0); // Start fully transparent
        fadeIn.setToValue(1.0);   // Fade to fully visible
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }
}

