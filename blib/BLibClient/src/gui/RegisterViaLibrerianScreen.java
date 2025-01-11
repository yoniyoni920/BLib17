package gui;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;




import java.io.IOException;

import base.Action;
import base.ClientApplication;
import base.ClientController;
import base.LibraryClient;
import entities.Message;
import entities.Role;
import entities.Subscriber;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.PasswordField;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
public class RegisterViaLibrerianScreen extends AbstractScreen {
	
	

	
	@FXML
	private PasswordField PassTxt;
	@FXML
	private PasswordField Password2;
	@FXML
	private TextField FName;
	@FXML
	private TextField LName;
	@FXML
	private TextField PhoneNumber;
	@FXML
	private TextField Email;
	@FXML
	private Label pass2ErrorLabel;
	@FXML
	private Label passErrorLabel;
	@FXML
	private Label FnameErrorLabel;
	@FXML
	private Label LNameErrorLabel;
	@FXML
	private Label EmailErrorLabel;
	@FXML
	private Label PhoneErrorLabel;
	@FXML
	private Label LibraranNamefx;
	
	

	public void startUp(String name) throws Exception {
		LibraranNamefx.setText(name);
	}
	
	public void RegisterSubscriber() {
		String pass2 = Password2.getText();
		String pass = PassTxt.getText();
		String FirstName = FName.getText();
		String LastName = LName.getText();
		String Phone = PhoneNumber.getText();
		String EmailAddress = Email.getText();
		boolean pass2Empty = pass2.trim().isEmpty();
		boolean passEmpty = pass.trim().isEmpty();
		boolean FirstNameEmpty = FirstName.trim().isEmpty();
		boolean LastNameEmpty = LastName.trim().isEmpty();
		boolean PhoneEmpty = Phone.trim().isEmpty();
		boolean EmailAddressEmpty = EmailAddress.trim().isEmpty();
		boolean valid = true;
		pass2ErrorLabel.setVisible(false);
		passErrorLabel.setVisible(false);
		if(pass2Empty)
		{
			pass2ErrorLabel.setText("You must enter an Password");
			pass2ErrorLabel.setVisible(true);
		}
		if(passEmpty)
		{
			passErrorLabel.setText("You must enter a password");
			passErrorLabel.setVisible(true);
		}
		if(!(pass2.equals(pass)))
		{
			passErrorLabel.setText("Passwords must be identical");
			passErrorLabel.setVisible(true);
		}
		
		if(FirstNameEmpty)
		{
			FnameErrorLabel.setText("You must enter a First Name");
			FnameErrorLabel.setVisible(true);
		}else if (!FirstName.matches("[a-zA-Z]+")) {
	        FnameErrorLabel.setText("First Name must contain only letters");
	        FnameErrorLabel.setVisible(true);
	        valid = false;
	    }
		
		
		
		
		
		if(LastNameEmpty)
		{
			LNameErrorLabel.setText("You must enter a Last Name");
			LNameErrorLabel.setVisible(true);
		}else if (!LastName.matches("[a-zA-Z]+")) {
	        LNameErrorLabel.setText("Last Name must contain only letters");
	        LNameErrorLabel.setVisible(true);
	        valid = false;
	    }
		if(PhoneEmpty)
		{
			PhoneErrorLabel.setText("You must enter a Phone Number");
			PhoneErrorLabel.setVisible(true);
		}else if (!Phone.matches("\\d+")) {
	        PhoneErrorLabel.setText("Must contain only numbers");
	        PhoneErrorLabel.setVisible(true);
	        valid = false;
	    }
		if(EmailAddressEmpty)
		{
			EmailErrorLabel.setText("You must enter a Email");
			EmailErrorLabel.setVisible(true);
		}else if (!EmailAddress.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
	        EmailErrorLabel.setText("Invalid Email format");
	        EmailErrorLabel.setVisible(true);
	        valid = false;
	    }
		
		
		
		if (!pass2Empty && !passEmpty && !FirstNameEmpty && !LastNameEmpty && !PhoneEmpty && !EmailAddressEmpty && ((pass2.equals(pass)) && valid))
		{
			// Attempt to Send message to Server To REGISTER USER
			Message msg = ClientApplication.chat.sendToServer(new Message(Action.REGISTER, new String[]{ pass,FirstName,LastName,Phone,EmailAddress }));
			
			
			if(!msg.isError()) {
				FnameErrorLabel.setVisible(false);
				User user = ((User)msg.getObject());
					LibrarianMainScreen LibMainScreen;
					try {
						LibMainScreen = (LibrarianMainScreen)screenManager.openScreen("LibrarianMainScreen", "Librarian Main Screen");
						LibMainScreen.loadUser(user);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			
			else {
				FnameErrorLabel.setText(msg.getObject() + "");
				FnameErrorLabel.setVisible(true);
			}
		}
	}


	/*
	 * close Window and returns to previous screen
	 */
	
	public void Exit(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}
}