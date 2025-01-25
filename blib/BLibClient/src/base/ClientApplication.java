package base;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import entities.Message;
import gui.ScreenManager;
import java.io.IOException;
/**
 * The runnable for client side.
 */
public class ClientApplication extends Application {
	private ScreenManager screenManager;
	private static ClientApplication clientApplication;
	private LibraryClient libraryClient;

	 /**
     * The main entry point of the client.
     * 
     * @param args command-line arguments
     * @throws Exception if an error occurs during application launch
     */
	public static void main(String args[]) {
	    launch(args);  
	}
	
	  /**
     * Gets the ScreenManager instance.
     * 
     * @return the ScreenManager instance
     */
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

		String ip = System.getProperty("ip", "localhost");
		String port = System.getProperty("port");
		if (port != null) {
			createClient(ip, Integer.valueOf(port));
		} else {
			screenManager.openScreen("ClientSocket", "Client Socket");
		}


		setUserAgentStylesheet("/gui/Main.css");
		primaryStage.getIcons().add(new Image(getClass().getResource("/resources/icon.png").toExternalForm()));
	}

	
	
	 /**
     * Creates and initializes the LibraryClient instance to connect to the server.
     * 
     * @param ip   the server's IP address
     * @param port the server's port number
     * @throws Exception if an error occurs during client creation
     */
	public void createClient(String ip, int port) throws Exception{
		try {
			libraryClient = new LibraryClient(ip, port);
		} catch (IOException e) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");

			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Could not start BLib client!");
			alert.setHeaderText(String.format("Couldn't find any server at: %s:%d", ip, port));
			alert.setContentText(e.toString());
			alert.showAndWait();
			System.exit(1);
		}

		screenManager.openScreen("LogInScreen", "Log In");
	}


    /**
     * Gets the singleton instance of ClientApplication.
     * 
     * @return the singleton ClientApplication instance
     */
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
