package base;

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
	private static ClientApplication clientApplication;
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
		clientApplication = this;
		screenManager = new ScreenManager(primaryStage);
		screenManager.openScreen("ClientSocket", "Client Socket Screen");
		
		setUserAgentStylesheet("/gui/Main.css");
	}
	
	public void createClient( String ip, int port) throws Exception{
		ClientApplication.chat = new ClientController(ip, Integer.valueOf(port));
		screenManager.openScreen("LogInScreen", "Log In Screen");
	}
	@Override
	public void stop() {
		chat.getClient().quit();
	}
	public static ClientApplication getInstance () {
		return clientApplication;
	}
}
