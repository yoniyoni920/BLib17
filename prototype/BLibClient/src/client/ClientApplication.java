package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Vector;

import gui.ScreenManager;

public class ClientApplication extends Application {
	public static ClientController chat; //only one instance
	private ScreenManager screenManager;
	
	public static void main(String args[]) throws Exception { 
	    launch(args);  
	}
	
	public ScreenManager getScreenManager() {
		return screenManager;
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		screenManager = new ScreenManager(primaryStage);
		screenManager.openScreen("LogInScreen", "Login");
		chat = new ClientController("localhost", 5555);
	}
	
	@Override
	public void stop() {
		chat.getClient().quit();
	}
}
