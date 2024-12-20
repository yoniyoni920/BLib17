package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LibraryClient;
import entities.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SubscriberMainScreen {
	private Subscriber subscriber;
		
	@FXML
	private Label idLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private Label historyLabel;
	@FXML
	private Label phoneLabel;
	@FXML
	private Label emailLabel;	
	
	ObservableList<String> list;
		
	public void loadSubscriber(Subscriber sub) {
		subscriber=sub;
		idLabel.setText(sub.getId());
		nameLabel.setText(sub.getFirstName());
		historyLabel.setText("" +sub.getDetailedSubscriptionHistory());
		phoneLabel.setText(sub.getPhoneNumber());
		emailLabel.setText(sub.getEmail());
	}
	
	public void closeWindow(ActionEvent event) throws Exception  {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/LogInScreen.fxml").openStream());	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/LogInScreen.css").toExternalForm());
		primaryStage.setTitle("Log In");
		primaryStage.setScene(scene);		
		primaryStage.show();
	    
	}
	public void openConfigureScreen(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/SubscriberSettingsScreen.fxml").openStream());
		SubscriberSettingsScreen subscriberSettingsScreen = loader.getController();
		subscriberSettingsScreen.loadSubscriber(subscriber);
		Scene scene = new Scene(root);			
		primaryStage.setTitle("Subscriber Settings");
		primaryStage.setScene(scene);		
		primaryStage.show();
	}
}
