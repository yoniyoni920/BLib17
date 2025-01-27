package base;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import gui.ScreenManager;
import gui.ServerGUI;
/**
 * ServerApplication serves as the entry point for the server-side application.
 * It manages the initialization of the server, GUI, and other essential components.
 */
public class ServerApplication extends Application {
	private static ServerApplication serverApplication;
	private ServerGUI serverGUI;
	private ScreenManager screenManager;
	private LibraryServer libraryServer;
	private JobManager jobManager;

	Timer timer;
	 /**
     * The main method serves as the entry point of the application.
     * It launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application
     * @throws Exception if an error occurs during application launch
     */
	public static void main(String args[]) {
		launch(args);
	}
	 /**
     * Initializes and starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application
     * @throws Exception if an error occurs during initialization
     */
	@Override
	public void start(Stage primaryStage) throws Exception {
		serverApplication = this;
		screenManager = new ScreenManager(primaryStage);

		String port = System.getProperty("port");
		if (port != null) {
			createServer(port);
		} else {
			screenManager.openScreen("ServerSocket", "Server Socket"); // Get controller for communicating with it later
		}

		setUserAgentStylesheet("/gui/Main.css");
		primaryStage.getIcons().add(new Image(getClass().getResource("/resources/icon.png").toExternalForm()));
	}
	/**
     * Creates and initializes the library server at the specified port.
     * Handles errors related to server initialization and updates the GUI.
     *
     * @param port The port number on which the server will run
     * @throws IOException if an error occurs while creating the server
     */
	public void createServer(String port) throws IOException {
		libraryServer = new LibraryServer(Integer.valueOf(port));
		try {
			libraryServer.listen();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Could not start BLib server!");
			alert.setHeaderText("Attempted to start the server at port: " + port );
			alert.setContentText(e.toString());
			alert.showAndWait();
			System.exit(0);
		}
		 // Open the server console GUI
		serverGUI = (ServerGUI)screenManager.openScreen("ServerGUI", "BLib Server Console"); // Get controller for communicating with it later
		 // Start the job manager for background tasks
		jobManager = new JobManager(); // Start the job manager

		timer = new Timer();
		// Check every 2 seconds for connections. May be useful for the reports generation ^^
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				serverGUI.updateConnections(libraryServer.getClientConnections());
			}
		}, 0, 2 * 1000);// Update every 2 seconds
	}
	/**
     * Provides the singleton instance of the ServerApplication.
     *
     * @return The current instance of ServerApplication
     */
	public static ServerApplication getInstance () {
		return serverApplication;
	}
	/**
     * Stops the application and performs cleanup.
     * Ensures that the server is stopped and the timer is canceled.
     *
     * @throws Exception if an error occurs during cleanup
     */
	@Override
	public void stop() throws Exception {
	   if (libraryServer != null) {
            libraryServer.stopListening();
       }
       System.exit(0);
       timer.cancel();     
	}

    public JobManager getJobManager() {
        return jobManager;
    }
}
