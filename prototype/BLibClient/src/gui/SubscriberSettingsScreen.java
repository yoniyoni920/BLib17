package gui;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import client.ClientApplication;
import client.LibraryClient;
import entities.Message;
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

public class SubscriberSettingsScreen extends AbstractScreen{
	@FXML
	private TextField phoneTxtField;
	@FXML
	private TextField emailTxtField;
	Subscriber sub;
	public void loadSubscriber(Subscriber sub) {
		this.sub=sub;
		phoneTxtField.setText(sub.getPhoneNumber());
		emailTxtField.setText(sub.getEmail());
	}
	public void updateInfo(ActionEvent event) throws Exception{
		String[] changedInfo = new String[3];   
		changedInfo[0] = phoneTxtField.getText();
		changedInfo[1] = emailTxtField.getText();
		changedInfo[2] = (String)sub.getId();
		
		Message msg = ClientApplication.chat.sendToServer(new Message("update", changedInfo));
		if( !msg.isError() ) {
			sub.setPhoneNumber(changedInfo[0]);
			sub.setEmail(changedInfo[1]);
		}
		close();
	}
	

	public void backBtn(ActionEvent event) throws Exception{
		close();
		 
	}
	
	public void close() throws Exception{
		 SubscriberMainScreen prevScreen = (SubscriberMainScreen)screenManager.closeScreen();
		 prevScreen.loadSubscriber(sub);
		 
	}
	
	
}
