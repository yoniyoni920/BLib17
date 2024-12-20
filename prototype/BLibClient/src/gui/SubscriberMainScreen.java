package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.LibraryClient;
import client.ClientApplication;
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
import logic.Faculty;
import logic.Student;

public class SubscriberMainScreen implements Initializable {
	private Student s;
		
	@FXML
	private Label lblName;
	@FXML
	private Label lblSurname;
	@FXML
	private Label lblFaculty;
	
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtSurname;
	@FXML
	private TextField idTextBox;
	
	@FXML
	private Button btnclose=null;	
	
	ObservableList<String> list;
		
	public void loadStudent(Student s1) {
		this.s=s1;
		this.idTextBox.setText(s.getId());
		this.txtName.setText(s.getPName());
		this.txtSurname.setText(s.getLName());		
	}
	
/*	public void saveStudent(ActionEvent event) throws Exception{
		Student newSt = new Student(s.getId(), txtName.getText(), txtSurname.getText(), s.getFc());
		if(!s.Equals(newSt)) {
			ClientApplication.chat.update(newSt);
		}
	}*/
	
	public void closeWindow(ActionEvent event) throws Exception  {
		FXMLLoader loader = new FXMLLoader();
		((Node)event.getSource()).getScene().getWindow().hide(); //hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/AcademicFrame.fxml").openStream());
	
		Scene scene = new Scene(root);			
		scene.getStylesheets().add(getClass().getResource("/gui/AcademicFrame.css").toExternalForm());
		primaryStage.setTitle("Academic Managment Tool");

		primaryStage.setScene(scene);		
		primaryStage.show();
	    
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
