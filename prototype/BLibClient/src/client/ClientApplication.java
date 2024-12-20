package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Vector;

public class ClientApplication extends Application {
	public static ClientController chat; //only one instance

	public static void main(String args[]) throws Exception { 
	    launch(args);  
	}
	 
	@Override
	public void start(Stage primaryStage) throws Exception {
		chat = new ClientController("localhost", 5555);			  		
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LogInScreen.fxml"));	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/LogInScreen.css").toExternalForm());
		primaryStage.setTitle("Log In");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void stop() {
		chat.getClient().quit();
	}
}
