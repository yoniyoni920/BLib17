package gui.librarian;


import base.Action;
import entities.BookCopy;
import entities.Message;
import gui.AbstractScreen;
import gui.SubscriberCardScreen;
import gui.subscriber_main_screen.BorrowedBookScreen;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import services.ClientUtils;
/**
 * This class manages the screen for extending the borrow time of a book copy.
 * It provides functionalities to:
 * - Display book details and welcome text
 * - Validate user input for the number of extension days
 * - Extend the borrow time for a book copy
 * - Navigate back to the previous screen upon successful extension
 */
public class ExtendBorrowTimeScreen extends AbstractScreen{
	
	@FXML
	private Label welcomeText;
	@FXML
	private Label bookNameLabel;
	@FXML
	private Label errorMessageLabel;
	@FXML
	private TextField daysExtension ;
	@FXML
	private Label descriptionLabel ;
	
	private BookCopy copy ;

    /**
     * Initializes the screen with the provided book copy details.
     *
     * @param copy the book copy whose borrow time is to be extended
     */
	public void onStart(BookCopy copy) {
		loadData(copy);
		renderData(); 
	}
	
	private void loadData(BookCopy copy) {
		this.copy = copy;
	}
	/**
     * Loads the book copy data into the screen's state.
     *
     * @param copy the book copy whose data is to be loaded
     */
//    private void loadData(BookCopy copy) {
//        this.copy = copy;
//    }

    /**
     * Renders the loaded book copy data onto the screen.
     * Also triggers a fade-in transition for the welcome message.
     */
	private void renderData() {
		fadeInLabelTransition(welcomeText);
		bookNameLabel.setText(copy.getBook().getTitle());
		descriptionLabel.setText("Borrowed From " + copy.getLendDate() + " to " + copy.getReturnDate() );
	}
	 /**
     * Closes the current window and navigates back to the BorrowedBookScreen.
     * Updates the previous screen with the latest book copy details.
     *
     * @param event the ActionEvent triggered by the close button
     * @throws Exception if an error occurs during screen transition
     */
	public void closeWindow(ActionEvent event) throws Exception {
		SubscriberCardScreen prevScreen = (SubscriberCardScreen) screenManager.closeScreen();
    }
    /**
     * Triggers a fade-in animation for the specified label.
     *
     * @param label the label to apply the fade-in animation to
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
    /**
     * Handles the extension of borrow time for the current book copy.
     * Validates the input, updates the return date, and sends the extension request to the server.
     *
     * @param event the ActionEvent triggered by the extend button
     */
	public void extendBorrowTime(ActionEvent event) {
		boolean succesfullyChanged = false ;
		int days = 0 ;
		if(checkExtendDaysInput()) {
			days = Integer.parseInt(daysExtension.getText());
			copy.setReturnDate(copy.getReturnDate().plusDays(days));
			succesfullyChanged = (boolean) ClientUtils.sendMessage(new Message(Action.EXTEND_BORROW_TIME , copy)).getObject();
			if(succesfullyChanged) {
				try {
					closeWindow(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				copy.setReturnDate(copy.getReturnDate().minusDays(days));
			}
		}
	}
	   /**
     * Validates the user input for the number of extension days.
     * Ensures that the input is a valid integer and within the allowed range.
     * Displays error messages for invalid inputs.
     *
     * @return the number of days entered by the user, or 0 for invalid input
     */
	private boolean checkExtendDaysInput() {
		int days = 0;
		try {
			days =Integer.parseInt(daysExtension.getText());
		}catch(NumberFormatException ex) {
			//just to make sure not to throw the exception
		}
		if(days > 14) {
			errorMessageLabel.setText("The Extension Time Should Be Maximum 14");
			errorMessageLabel.setVisible(true);
			return false ;
		}
		if(days <= 0) {
			errorMessageLabel.setText("Please enter a valid extension days");
			errorMessageLabel.setVisible(true);
			return false ;
		}
		return true ;
	}
}
