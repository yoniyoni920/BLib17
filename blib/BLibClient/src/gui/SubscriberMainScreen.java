package gui;



import entities.Subscriber;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class SubscriberMainScreen extends AbstractScreen{
	private Subscriber subscriber;
	
	@FXML
	private Label welcomeText ;
	@FXML
	private MenuButton menuButton;
	@FXML 
	private MenuItem infoMenuItem ;
	@FXML 
	private MenuItem historyMenuItem ;
	@FXML 
	private MenuItem signoutMenuItem ;
	@FXML
	private ListView<Pane> borrowedBooksListView ;
	
	ObservableList<String> list;
		/*
		 * load Subscriber information to the scene
		 */
	public void loadSubscriber(Subscriber sub) {
	    subscriber = sub;
	    welcomeText.setText("Welcome back, " + sub.getName());
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
	        fadeInMyBooks.play(); // Play the fade-in transition for "My Books"
	    });

	    // Ensure no looping happens
	    fadeInMyBooks.setOnFinished(event -> {
	        // Do nothing, just stop the cycle here
	    });

	    // Start the first fade-in transition
	    fadeIn.setOnFinished(event -> {
	        // Pause briefly, then start fade-out
	        PauseTransition pause = new PauseTransition(Duration.seconds(3));
	        pause.setOnFinished(e -> fadeOut.play());
	        pause.play();
	    });

	    fadeIn.play();
	    showBorrowedBooks() ;
	}
	/*
	 * close Window and returns to previous screen
	 */
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}
	
	
	/*
	 * 
	 * open
	 */
	public void openSubInfoScreen(ActionEvent event) throws Exception {
		SubInfoScreen screen = (SubInfoScreen)screenManager.openScreen("SubInfoScreen", "Subscriber Info Screen");
		screen.loadSubscriber(subscriber);
	}
	
	
	//not finished 
	public void showBorrowedBooks() {
        // Create Pane objects with placeholder (Rectangle) and rounded edges
        Pane pane1 = new StackPane();
        Rectangle rect1 = new Rectangle(200, 300);  // Book cover size (width x height)
        rect1.setFill(Color.LIGHTBLUE);  // Set placeholder color
        rect1.setStroke(Color.BLACK);  // Add border to the rectangle
        rect1.setArcWidth(20);  // Set horizontal radius for rounded corners
        rect1.setArcHeight(20);  // Set vertical radius for rounded corners
        pane1.getChildren().add(rect1);  // Add the rectangle to the pane
        pane1.setPrefSize(200, 300);  // Set the Pane's preferred size

        Pane pane2 = new StackPane();
        Rectangle rect2 = new Rectangle(200, 300);  // Book cover size
        rect2.setFill(Color.LIGHTGREEN);  // Set placeholder color
        rect2.setStroke(Color.BLACK);  // Add border to the rectangle
        rect2.setArcWidth(20);  // Set horizontal radius for rounded corners
        rect2.setArcHeight(20);  // Set vertical radius for rounded corners
        pane2.getChildren().add(rect2);  // Add the rectangle to the pane
        pane2.setPrefSize(200, 300);  // Set the Pane's preferred size

        Pane pane3 = new StackPane();
        Rectangle rect3 = new Rectangle(200, 300);  // Book cover size
        rect3.setFill(Color.LIGHTCORAL);  // Set placeholder color
        rect3.setStroke(Color.BLACK);  // Add border to the rectangle
        rect3.setArcWidth(20);  // Set horizontal radius for rounded corners
        rect3.setArcHeight(20);  // Set vertical radius for rounded corners
        pane3.getChildren().add(rect3);  // Add the rectangle to the pane
        pane3.setPrefSize(200, 300);  // Set the Pane's preferred size

        // Add Panes to ObservableList
        ObservableList<Pane> panes = FXCollections.observableArrayList(pane1, pane2, pane3);

        // Set the ListView items
        borrowedBooksListView.setItems(panes);

        // Set the CellFactory to display each Pane in the ListView
        borrowedBooksListView.setCellFactory(param -> new ListCell<Pane>() {
            @Override
            protected void updateItem(Pane pane, boolean empty) {
                super.updateItem(pane, empty);

                if (pane != null && !empty) {
                    setGraphic(pane);  // Set the Pane as the graphic for the ListCell
                } else {
                    setGraphic(null);  // Clear the cell if it's empty
                }
            }
        });
    }
}


