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

/*
 * controller for ClientSocket.fxml
 */
public class ClientSocket extends AbstractScreen {	
	@FXML
	private TextField IPtxt = null;
	@FXML
	private TextField Porttxt = null;
	
	/*
	 * connects to server with wanted Ip and port
	 */
	public void Connect(ActionEvent event) throws Exception {
		String ip,port;
		ip=IPtxt.getText();
		port = Porttxt.getText();
		if(ip.trim().isEmpty()|| port.trim().isEmpty())
		{

			System.out.println("You must enter an IP and port number");	
		}
		else
		{
			ClientApplication.chat = new ClientController(ip, Integer.valueOf(port));
			screenManager.openScreen("LogInScreen", "Log In Screen");
			
		}
	}
	
}
