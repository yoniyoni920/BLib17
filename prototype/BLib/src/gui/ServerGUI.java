package gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerGUI  {		
	@FXML
	private Button btnExit = null;
	@FXML
	private Button btnDone = null;
	@FXML
	private Label lbllist;
	
	@FXML
	private TextField portxt;
	ObservableList<String> list;
	
	public void Done(ActionEvent event) throws Exception {
		
	}

	public void start(Stage primaryStage) throws Exception {	
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/gui/ServerGUI.fxml")));
		scene.getStylesheets().add(getClass().getResource("/gui/ServerGUI.css").toExternalForm());

		primaryStage.setTitle("BLib Server Interface");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void getExitBtn(ActionEvent event) throws Exception {
		System.exit(0);			
	}
}