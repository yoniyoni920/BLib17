package gui.subscriber_main_screen;


import base.Action;
import entities.BookCopy;
import entities.Message;
import gui.AbstractScreen;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import services.ClientUtils;

public class ExtendBorrowTimeScreen extends AbstractScreen{
	
	@FXML
	private Label welcomeText;
	@FXML
	private Label bookNameLabel;
	@FXML
	private Label errorMessageLabel;
	@FXML
	private TextField daysExtension ;
	
	private BookCopy copy ;
	
	public void onStart(BookCopy copy) {
		loadData(copy);
		renderData(); 
	}
	
	private void loadData(BookCopy copy) {
		this.copy = copy;
	}
	
	private void renderData() {
		fadeInLabelTransition(welcomeText);
		bookNameLabel.setText(copy.getBook().getTitle());
	}
	
	public void closeWindow(ActionEvent event) throws Exception {
		BorrowedBookScreen prevScreen = (BorrowedBookScreen) screenManager.closeScreen();
        prevScreen.onStart(copy);
    }
	
	private void fadeInLabelTransition(Label welcomeText) {
        welcomeText.setOpacity(0.0); // Start with the text invisible

        // First Fade-In Transition (Welcome Message)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeIn.setFromValue(0.0); // Start fully transparent
        fadeIn.setToValue(1.0);   // Fade to fully visible
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }
	
	public void extendBorrowTime(ActionEvent event) {
		boolean succesfullyChanged = false ;
		int days = returnAndCheckDaysInput();
		if(days > 0) {
			copy.setReturnDate(copy.getReturnDate().plusDays(days));
			succesfullyChanged = (boolean) ClientUtils.sendMessage(new Message(Action.EXTEND_BORROW_TIME , copy)).getObject();
		}
		if(succesfullyChanged) {
			try {
				closeWindow(event);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private int returnAndCheckDaysInput() {
		int days = 0;
		try {
			days =Integer.parseInt(daysExtension.getText());
		}catch(NumberFormatException ex) {
			//just to make sure not to throw the exception
		}
		if(days > 14) {
			errorMessageLabel.setText("The Extension Time Should Be Maximum 14");
			errorMessageLabel.setVisible(true);
		}
		if(days <= 0) {
			errorMessageLabel.setText("Please enter a valid extension days");
			errorMessageLabel.setVisible(true);
		}
		return days ;
	}
}
