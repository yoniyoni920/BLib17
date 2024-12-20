package gui;

import java.io.IOException;


import client.LibraryClient;
import client.ClientController;
import client.ClientApplication;
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


public  class LogInScreen   {
	private SubscriberMainScreen sfc;	
	private static int itemIndex = 3;
	
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
				System.out.print("Error in message handling");
			}
			else {
				Subscriber sub = ((Subscriber)msg.getObject());
				((Node)event.getSource()).getScene().getWindow().hide();//hiding primary window
				Stage primaryStage = new Stage();
				Pane root = loader.load(getClass().getResource("/gui/SubscriberMainScreen.fxml").openStream());
				SubscriberMainScreen subscriberMainScreen = loader.getController();
				subscriberMainScreen.loadSubscriber(sub);
				Scene scene = new Scene(root);			
				scene.getStylesheets().add(getClass().getResource("/gui/SubscriberMainScreen.css").toExternalForm());
				primaryStage.setTitle("Main Screen");
				primaryStage.setScene(scene);		
				primaryStage.show();
			}
		
//			if(LibraryClient.s1.getId().equals("Error"))
//			{
//				System.out.println("Student ID Not Found");
//				
//			}
//			else {
//				System.out.println("Student ID Found");
//				((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
//				Stage primaryStage = new Stage();
//				Pane root = loader.load(getClass().getResource("/gui/StudentForm.fxml").openStream());
//				StudentFormController studentFormController = loader.getController();		
//				studentFormController.loadStudent(LibraryClient.s1);
//			
//				Scene scene = new Scene(root);			
//				scene.getStylesheets().add(getClass().getResource("/gui/StudentForm.css").toExternalForm());
//				primaryStage.setTitle("Student Managment Tool");
//	
//				primaryStage.setScene(scene);		
//				primaryStage.show();
//			}
		}
	}
	
}
