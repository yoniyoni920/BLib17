package gui;

import java.io.IOException;
import java.util.List;

import base.Action;
import base.ClientApplication;
import base.ClientController;
import base.LibraryClient;
import entities.BookCopy;
import entities.Message;
import entities.Role;
import entities.Subscriber;
import entities.User;
import gui.subscriber_main_screen.SubscriberMainScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * controller for LogInScreen.fxml
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
	

	/*
	 * gets the info from fields and sends it
	 * to be Checked by the system and move to the 
	 * main Screen after being logged in
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
			Message msg = ClientApplication.chat.sendToServer(new Message(Action.LOGIN, new String[]{ id, pass }));
			if(!msg.isError()) {
				idErrorLabel.setVisible(false);
				User user = ((User)msg.getObject());

				// Check which user this is to show the appropriate screen
				if (user.getRole() == Role.SUBSCRIBER) {
					Message msg2 = ClientApplication.chat.sendToServer(new Message(Action.RETRIEVE_BORROWEDBOOKS, (Subscriber) user));
					SubscriberMainScreen subMainScreen = (SubscriberMainScreen)screenManager.openScreen("subscriber_main_screen/SubscriberMainScreen", "Subscriber Main Screen");
					subMainScreen.onStart((Subscriber)user ,(List<BookCopy>) msg2.getObject());
				} else {
					LibrarianMainScreen libMainScreen = (LibrarianMainScreen)screenManager.openScreen("LibrarianMainScreen", "Librarian Main Screen");
					libMainScreen.loadUser(user);
				}
			}
			else {
				idErrorLabel.setText(msg.getObject() + "");
				idErrorLabel.setVisible(true);
			}
		}
	}
	public void searchBooksScreen(ActionEvent event) throws Exception{
		screenManager.openScreen("SearchBooksScreen", "Search Book");
	}
	
}
