package gui;

import base.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * controller for ClientSocket.fxml
 * This class manages the user interface for connecting to the server.
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
	
	  /**
     * Handles the action of connecting to the server using the specified IP and port.
     * 
     * @param event the action event triggered by clicking the "Connect" button
     * @throws Exception if an error occurs while creating the client connection
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
		if(flag)  // If both inputs are valid, create the client connection
		{
			ClientApplication.getInstance().createClient(ip, Integer.valueOf(port));
			
		}
	}
	
}
