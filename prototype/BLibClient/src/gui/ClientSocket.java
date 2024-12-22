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
 * controller for ClientSocket.fxml
 */
public class ClientSocket extends AbstractScreen {	
	@FXML
	private TextField IPtxt = null;
	@FXML
	private TextField Porttxt = null;
	
	@FXML 
	private Label errorIp;
	
	@FXML
	private Label errorPort;
	
	/*
	 * connects to server with wanted Ip and port
	 */
	public void Connect(ActionEvent event) throws Exception {
		String ip,port;
		boolean flag=true;
		ip=IPtxt.getText();
		port = Porttxt.getText();
		errorIp.setVisible(false);
		errorPort.setVisible(false);
		if(ip.trim().isEmpty())
		{
			errorIp.setVisible(true);
			flag = false;
			
		}
		if(port.trim().isEmpty()) {
			errorPort.setVisible(true);
			flag = false;
		}
		if(flag)
		{
			ClientApplication.getInstance().createClient(ip, Integer.valueOf(port));
			
		}
	}
	
}
