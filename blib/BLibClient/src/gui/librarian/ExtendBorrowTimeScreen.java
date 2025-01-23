package gui.librarian;


import base.Action;
import entities.BookCopy;
import entities.Message;
import gui.AbstractScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ClientUtils;
import services.InterfaceUtils;

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

	/**
	 * Loads the book copy data into the screen's state.
	 *
	 * @param copy the book copy whose data is to be loaded
	 */
	private void loadData(BookCopy copy) {
		this.copy = copy;
	}

    /**
     * Renders the loaded book copy data onto the screen.
     * Also triggers a fade-in transition for the welcome message.
     */
	private void renderData() {
		bookNameLabel.setText(copy.getBook().getTitle());
		descriptionLabel.setText(String.format("Borrowing From %s Until %s",
				InterfaceUtils.formatDate(copy.getLendDate()), InterfaceUtils.formatDate(copy.getReturnDate())));
	}

    /**
     * Handles the extension of borrow time for the current book copy.
     * Validates the input, updates the return date, and sends the extension request to the server.
     *
     * @param event the ActionEvent triggered by the extend button
     */
	public void extendBorrowTime(ActionEvent event) {
		int days = 0;
		if(checkExtendDaysInput()) {
			days = Integer.parseInt(daysExtension.getText());
			copy.setReturnDate(copy.getReturnDate().plusDays(days));
			Message msg = ClientUtils.sendMessage(new Message(Action.EXTEND_BORROW_TIME , copy));

			// Sucess dialog
			Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
			successAlert.setTitle("Extend Borrow Duration");
			successAlert.setHeaderText("Success");
			successAlert.setContentText("Successfully extended the borrow duration!");
			successAlert.showAndWait();

			if(!msg.isError()) {
				try {
					closeScreen(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				Alert erorrAlert = new Alert(Alert.AlertType.ERROR);
				erorrAlert.setTitle("Extend Borrow Time");
				erorrAlert.setHeaderText("Failed to extend borrow time!");
				erorrAlert.showAndWait();
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
