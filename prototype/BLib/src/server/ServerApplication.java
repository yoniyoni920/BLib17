package server;

import javafx.application.Application;
import javafx.stage.Stage;
import gui.ServerGUI;

public class ServerApplication extends Application {
	final public static int DEFAULT_PORT = 5555;

	public static void main(String args[]) throws Exception {   
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerGUI serverGUI = new ServerGUI(); // create StudentFrame
		serverGUI.start(primaryStage);

		LibraryServer sv = new LibraryServer(DEFAULT_PORT);

		try {
			sv.listen();
		} catch (Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
