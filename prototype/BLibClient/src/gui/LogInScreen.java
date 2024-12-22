package gui;

import java.io.IOException;


import client.LibraryClient;
import client.ClientApplication;
import client.ClientController;
import entities.Message;
import entities.Subscriber;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	private Label errorLabel;
	
	/*
	 * gets the info from fields and sends it
	 * to be Checked by the system and move to the 
	 * main Screen after being logged in
	 */
	public void Login(ActionEvent event) throws Exception {
		String id;
		FXMLLoader loader = new FXMLLoader();
		
		id=idtxt.getText();
		if(id.trim().isEmpty())
		{
			errorLabel.setText("You must enter an ID number");	
			errorLabel.setVisible(true);
		}
		else
		{
			Message msg = ClientApplication.chat.sendToServer(new Message("login", id));
			if(msg.isError()) {
				errorLabel.setText("Could not Find ID");
				errorLabel.setVisible(true);
			}
			else {
				errorLabel.setVisible(false);
				Subscriber sub = ((Subscriber)msg.getObject());
				SubscriberMainScreen subMainScreen = (SubscriberMainScreen)screenManager.openScreen("SubscriberMainScreen", "Subscriber Main Screen");
				subMainScreen.loadSubscriber(sub);
			}
		}
	}
	
}
