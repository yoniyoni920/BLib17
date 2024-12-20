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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class LogInScreen extends AbstractScreen {	
	@FXML
	private Button btnExit = null;
	
	@FXML
	private Button btnSend = null;
	
	@FXML
	private TextField idtxt;
	
	public void Send(ActionEvent event) throws Exception {
		String id;
		FXMLLoader loader = new FXMLLoader();
		
		id=idtxt.getText();
		if(id.trim().isEmpty())
		{

			System.out.println("You must enter an id number");	
		}
		else
		{
			Message msg = ClientApplication.chat.sendToServer(new Message("login", id));
			if(msg.isError()) {
				System.out.print(msg.getObject());
			}
			else {
				Subscriber sub = ((Subscriber)msg.getObject());
				
				SubscriberMainScreen subMainScreen = (SubscriberMainScreen)screenManager.openScreen("SubscriberMainScreen", "Subscriber Main Screen");
				subMainScreen.loadSubscriber(sub);
			}
		}
	}
	
}
