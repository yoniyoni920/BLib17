package gui;

import java.util.List;

import base.Action;
import base.ClientApplication;
import entities.BookCopy;
import entities.Message;
import entities.Role;
import entities.Subscriber;
import entities.User;
import gui.librarian.LibrarianMainScreen;
import gui.subscriber_main_screen.SubscriberMainScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.ClientUtils;

/**
 * Controller for the LogInScreen.fxml file.
 * Handles user login functionality and navigation to the appropriate main screen based on the user's role.
 */
public class LogInScreen extends AbstractScreen {	
	@FXML
	private Button btnExit = null;
	
	@FXML
	private Button btnSend = null;
	
	@FXML
	private TextField idtxt;

	@FXML
	private PasswordField passTxt;
	
	@FXML
	private Label idErrorLabel;

	@FXML
	private Label passErrorLabel;
	

    /**
     * Handles the login process by sending user credentials to the server for validation.
     * Displays error messages if fields are empty or if login fails.
     * 
     * @param event the ActionEvent triggered by the login button
     * @throws Exception if an error occurs during the login process
     */
	@SuppressWarnings("unchecked")
	public void Login(ActionEvent event) throws Exception {
		String id = idtxt.getText();
		String pass = passTxt.getText();

		boolean idEmpty = id.trim().isEmpty();
		boolean passEmpty = pass.trim().isEmpty();

		idErrorLabel.setVisible(false);
		passErrorLabel.setVisible(false);

		if(idEmpty)
		{
			idErrorLabel.setText("You must enter an ID number");
			idErrorLabel.setVisible(true);
		}
		if(passEmpty)
		{
			passErrorLabel.setText("You must enter a password");
			passErrorLabel.setVisible(true);
		}

		if (!idEmpty && !passEmpty)
		{
			// Attempt to login via server
			Message msg = ClientUtils.sendMessage(new Message(Action.LOGIN, new String[]{ id, pass }));
			if(!msg.isError()) {
				idErrorLabel.setVisible(false);
				User user = ((User)msg.getObject());

				// Check which user this is to show the appropriate screen
				if (user.getRole() == Role.SUBSCRIBER) {
					SubscriberMainScreen subMainScreen = (SubscriberMainScreen)screenManager.openScreen("subscriber_main_screen/SubscriberMainScreen", "Subscriber Main Screen");
					subMainScreen.onStart((Subscriber)user);
				} else {
					LibrarianMainScreen libMainScreen = (LibrarianMainScreen)screenManager.openScreen("librarian/LibrarianMainScreen", "Librarian Main Screen");
					libMainScreen.loadUser(user);
					libMainScreen.startUp(user.getName());
				}
			}
			else {
				idErrorLabel.setText(msg.getObject() + "");
				idErrorLabel.setVisible(true);
			}
		}
	}
	
    /**
     * Navigates to the SearchBooksScreen.
     * 
     * @param event the ActionEvent triggered by the search books button
     * @throws Exception if an error occurs during navigation
     */
	public void searchBooksScreen(ActionEvent event) throws Exception{
		screenManager.openScreen("SearchBooksScreen", "Search Book");
	}
	
}
