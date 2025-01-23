package gui.subscriber_main_screen;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import base.Action;
import controllers.Auth;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import gui.AbstractScreen;
import gui.BookCard;
import gui.SubscriberCardScreen;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import services.ClientUtils;
import services.InterfaceUtils;

/**
 * This class represents the main screen for a library subscriber.
 * It manages the UI and functionality for the subscriber's dashboard, 
 * including borrowed books and navigation to other screens.
 * 
 * @author Helal Hammoud
 */
public class SubscriberMainScreen extends AbstractScreen {

    @FXML private Label frozenText;
    @FXML private HBox frozenBox;

    @FXML
    private Label welcomeText;

    @FXML
    private GridPane borrowedBooksGrid;

    @FXML
    private ScrollPane borrowedBooksScrollPane;

    @FXML
    private Label borrowedBooksCount;
    
    private Subscriber subscriber;

    // Used to dynamically store the borrowed books for this subscriber
    private ObservableList<BookCopy> borrowedBooks = FXCollections.observableArrayList();

    @Override
    public void openScreen(Object... args) {
        onStart((Subscriber)args[0]);
    }

    /**
     * Initializes the subscriber main screen.
     * This method sets up the screen with the subscriber's name and their borrowed books.
     *
     * @param sub The logged-in subscriber.
     */
    public void onStart(Subscriber sub) {
        final double SCROLL_SPEED = 0.005;
        borrowedBooksScrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SCROLL_SPEED;
            borrowedBooksScrollPane.setVvalue(borrowedBooksScrollPane.getVvalue() - deltaY);
        });

        loadData(sub);
        renderData();
    }

    /**
     * Loads the subscriber and their borrowed books into the screen.
     *
     * @param sub The logged-in subscriber.
     */
    private void loadData(Subscriber sub) {
        subscriber = sub;
        this.borrowedBooks.addAll(sub.getBorrowedBooks());
    }
    
    /**
     * Renders the data on the screen including the welcome message, borrowed books, and the count of borrowed books.
     */
    private void renderData() {
        welcomeText.setText("Welcome back, " + subscriber.getName());
        transitionPlayer(welcomeText);
        showBorrowedBooks();

        boolean isFrozen = subscriber.isFrozen();
        frozenBox.setManaged(isFrozen);
        frozenBox.setVisible(isFrozen);
        if (isFrozen) {
            frozenText.setText("Frozen Until " + InterfaceUtils.formatDate(subscriber.getFrozenUntil()));
        }

        borrowedBooksCount.setText("You Have " + borrowedBooks.size() + " Books in Your Library");
    }

    /**
     * Opens the subscriber information screen.
     *
     * @param event The action event triggered by the corresponding button.
     * @throws Exception If an error occurs during screen opening.
     */
    public void openSubInfoScreen(ActionEvent event) throws Exception {
        SubscriberCardScreen card = (SubscriberCardScreen)screenManager.openScreen("SubscriberCardScreen", "Subscriber Card");
        card.setData(subscriber, true);
    }

    /**
     * Opens the subscriber history screen.
     *
     * @param event The action event triggered by the corresponding button.
     * @throws Exception If an error occurs during screen opening.
     */
    public void openSubscriberHistoryScreen(ActionEvent event) throws Exception {
        SubscriberHistoryScreen screen = (SubscriberHistoryScreen) screenManager.openScreen("subscriber_main_screen/SubscriberHistoryScreen", "Subscriber History");
        screen.onStart(subscriber);
    }

    /**
     * Opens the book search screen.
     *
     * @param event The action event triggered by the corresponding button.
     * @throws Exception If an error occurs during screen opening.
     */
    public void openSearchBooksScreen(ActionEvent event) throws Exception {
        screenManager.openScreen("SearchBooksScreen", "Book Search");
    }

    /**
     * Opens the borrowed book details screen for a specific book.
     *
     * @param event The action event triggered by the corresponding button.
     * @param copy The borrowed book to display.
     * @throws Exception If an error occurs during screen opening.
     */
    public void openBorrowedBookScreen(ActionEvent event, BookCopy copy) throws Exception {
        screenManager.openScreen("subscriber_main_screen/BorrowedBookScreen", "Borrowed Book", copy, subscriber);
    }

    /**
     * Opens the Subscriber Settings screen and loads the necessary subscriber information into it.
     * This allows the subscriber to edit their information.
     *
     * @param event The action event triggered by the open configure screen action.
     * @throws Exception If an error occurs while opening the Subscriber Settings screen.
     */
    public void openConfigureScreen(ActionEvent event) throws Exception {
        screenManager.openScreen("subscriber_main_screen/SubscriberSettingsScreen", "Subscriber Settings", subscriber);
    }

    /**
     * Displays the list of books borrowed by the subscriber.
     * Sets up custom rendering and event handling for each item in the list.
     */
    private void showBorrowedBooks() {
        InterfaceUtils.makeGrid(borrowedBooksGrid, 3, borrowedBooks, copy -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookCard.fxml"));
            try {
                Node card = loader.load();
                BookCard bookCard = loader.getController();
                bookCard.setBookData(copy.getBook());
                bookCard.setExtraDetailsVisible(false);
                card.setOnMousePressed(event -> {
                    try {
                        openBorrowedBookScreen(null, copy);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                return card;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Plays a dynamic welcome text transition on the screen.
     * Includes fade-in and fade-out effects, changing the displayed message.
     *
     * @param welcomeText The welcome text label to animate.
     */
    private void transitionPlayer(Label welcomeText) {
        welcomeText.setOpacity(0.0); // Start with the text invisible

        // First Fade-In Transition (Welcome Message)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeIn.setFromValue(0.0); // Start fully transparent
        fadeIn.setToValue(1.0);   // Fade to fully visible
        fadeIn.setCycleCount(1);

        // Fade-Out Transition (Welcome Message)
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeOut.setFromValue(1.0); // Start fully visible
        fadeOut.setToValue(0.0);   // Fade to fully transparent
        fadeOut.setCycleCount(1);

        // Second Fade-In Transition (My Books)
        FadeTransition fadeInMyBooks = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeInMyBooks.setFromValue(0.0); // Start fully transparent
        fadeInMyBooks.setToValue(1.0);   // Fade to fully visible
        fadeInMyBooks.setCycleCount(1);

        // After fade-out, change the text and fade in "My Books"
        fadeOut.setOnFinished(event -> {
            welcomeText.setText("My Books");
            fadeInMyBooks.play();
        });

        // Start the first fade-in transition
        fadeIn.setOnFinished(event -> {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> fadeOut.play());
            pause.play();
        });

        fadeIn.play();
    }

    public void logout(ActionEvent event) {
        Auth.getInstance().setUser(null); // Clear current user
        closeScreen(event);
    }
}

