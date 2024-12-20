package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

import gui.ServerGUI;

public class ServerApplication extends Application {
	final public static int DEFAULT_PORT = 5555;

	private ServerGUI serverGUI;
	
	private LibraryServer libraryServer;
	
	Timer timer;

	public static void main(String args[]) throws Exception {   
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerGUI.fxml"));
		Scene scene = new Scene(loader.load());

		primaryStage.setTitle("BLib Server Console");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		serverGUI = loader.getController(); // Get controller for communicating with it later

		libraryServer = new LibraryServer(DEFAULT_PORT);
		
		try {
			libraryServer.listen();
		} catch (Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		
		timer = new Timer();
		
		// Check every 2 seconds for connections. May be useful for the reports generation ^^
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				serverGUI.updateConnections(libraryServer.getClientConnections());
			}
		}, 0, 2 * 1000);
	}
}
