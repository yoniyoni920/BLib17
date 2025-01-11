package base;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import gui.ScreenManager;
import gui.ServerGUI;

public class ServerApplication extends Application {
	private static ServerApplication serverApplication;
	private ServerGUI serverGUI;
	private ScreenManager screenManager;
	private LibraryServer libraryServer;
	
	Timer timer;

	public static void main(String args[]) throws Exception {   
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		serverApplication = this;
		screenManager = new ScreenManager(primaryStage);
		screenManager.openScreen("ServerSocket", "Server Socket Screen"); // Get controller for communicating with it later

		setUserAgentStylesheet("/gui/Main.css");
	}
	
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

		serverGUI = (ServerGUI)screenManager.openScreen("ServerGUI", "BLib Server Console"); // Get controller for communicating with it later

		new JobManager(); // Start the job manager

		timer = new Timer();
		// Check every 2 seconds for connections. May be useful for the reports generation ^^
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				serverGUI.updateConnections(libraryServer.getClientConnections());
			}
		}, 0, 2 * 1000);
	}
	
	public static ServerApplication getInstance () {
		return serverApplication;
	}

	@Override
	public void stop() throws Exception {
	   if (libraryServer != null) {
            libraryServer.stopListening();
       }
       System.exit(0);
       timer.cancel();     
	}
}
