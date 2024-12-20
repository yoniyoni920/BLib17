package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

import gui.ScreenManager;
import gui.ServerGUI;

public class ServerApplication extends Application {
	final private static int DEFAULT_PORT = 5555;

	private ServerGUI serverGUI;
	
	private LibraryServer libraryServer;
	
	Timer timer;

	public static void main(String args[]) throws Exception {   
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		libraryServer = new LibraryServer(DEFAULT_PORT);
		
		try {
			libraryServer.listen();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Could not start BLib server!");
			alert.setHeaderText("Attempted to start the server at port 5555");
			alert.setContentText(e.toString());
			alert.showAndWait();
			System.exit(0);
		}
		
		ScreenManager manager = new ScreenManager(primaryStage);
		serverGUI = (ServerGUI)manager.openScreen("ServerGUI", "BLib Server Console"); // Get controller for communicating with it later

		timer = new Timer();
		
		// Check every 2 seconds for connections. May be useful for the reports generation ^^
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				serverGUI.updateConnections(libraryServer.getClientConnections());
			}
		}, 0, 2 * 1000);
	}
	
	@Override
	public void stop() throws Exception {
		libraryServer.stopListening();
		System.exit(0);
	}
}
