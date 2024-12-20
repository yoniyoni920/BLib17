package client;
import javafx.application.Application;

import javafx.stage.Stage;
import logic.Faculty;
import logic.Student;

import java.util.Vector;

import client.ClientController;
import gui.LogInScreen;
import gui.SubscriberMainScreen;

public class ClientApplication extends Application {
	public static ClientController chat; //only one instance

	public static void main(String args[]) throws Exception { 
	    launch(args);  
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		 chat = new ClientController("localhost", 5555);
						  		
		LogInScreen aFrame = new LogInScreen(); // create StudentFrame
		aFrame.start(primaryStage);
	}
	
	@Override
	public void stop() {
		chat.client.quit();
	}
}
