package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Vector;

import gui.ScreenManager;
/*
 * The runnable for client side.
 * 
 */

/**
 * Constructs an instance of client Controller which 
 * starts LiberaryClient. 
 *
 * @param host The host to connect to.
 * @param port The port to connect on.
 */
public class ClientApplication extends Application {
	public static ClientController chat; //only one instance
	private ScreenManager screenManager;
	final static int default_port = 5555; 
	public static void main(String args[]) throws Exception { 
	    launch(args);  
	}
	
	public ScreenManager getScreenManager() {
		return screenManager;
	}
	/**
     * Constructs an instance of the ScreenManager .
     * and opens  Login screen
     */
     
	@Override
	public void start(Stage primaryStage) throws Exception {
		screenManager = new ScreenManager(primaryStage);
		screenManager.openScreen("LogInScreen", "Login");
		chat = new ClientController("localhost", default_port);
	}
	
	@Override
	public void stop() {
		chat.getClient().quit();
	}
}
