package base;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import entities.Message;
import gui.ScreenManager;
import java.io.IOException;
/*
 * The runnable for client side.
 * 
 */

/**
 * Constructs an instance of client Controller which 
 * starts LiberaryClient.
 */
public class ClientApplication extends Application {
	public static ClientController chat; //only one instance
	private ScreenManager screenManager;
	private static ClientApplication clientApplication;
	private LibraryClient libraryClient;

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
		ClientApplication.chat = new ClientController();

		try {
			libraryClient = new LibraryClient(ip, Integer.valueOf(port));
		} catch (IOException e) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Could not start BLib client!");
			alert.setHeaderText(String.format("Couldn't find any server at: %s:%d", ip, port));
			alert.setContentText(e.toString());
			alert.showAndWait();
			System.exit(1);
		}

		screenManager.openScreen("LogInScreen", "Log In Screen");
	}

	public static ClientApplication getInstance() {
		return clientApplication;
	}

	/**
	 * Sends a Message object to the server. For convenience this is kept here.
	 * However, you should use the utility methods instead of this directly.
	 * @param msg
	 * @return Message response from the server
	 */
	public Message sendMessageToServer(Message msg) {
		return libraryClient.sendMessageToServer(msg);
	}

}
