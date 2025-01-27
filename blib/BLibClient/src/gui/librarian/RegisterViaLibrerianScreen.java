package gui.librarian;

import gui.AbstractScreen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import base.Action;
import entities.Message;

import javafx.scene.control.PasswordField;

import services.ClientUtils;


/**
* This class handles the registration of subscribers by librarians.
* It provides functionality to validate user input, display error messages
* for invalid or missing fields, and send registration requests to the server.
* <br>
* The class includes methods to initialize the screen with the librarian's name,
* handle user registration, and close the registration screen.
*/
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
	
	/**
     * Initializes the screen with the librarian's name.
     *
     * @param name the name of the librarian
     * @throws Exception if an error occurs during setup
     */

	public void startUp(String name) throws Exception {
		LibraranNamefx.setText(name);
	}
	 /**
     * Handles the registration of a new subscriber.
     * Validates input fields, displays error messages if any validation fails,
     * and sends the registration request to the server if all inputs are valid.
     */
	public void registerSubscriber() {
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
		
		
	     // If all fields are valid, attempt to register
		if (!pass2Empty && !passEmpty && !FirstNameEmpty && !LastNameEmpty && !PhoneEmpty && !EmailAddressEmpty && ((pass2.equals(pass)) && valid))
		{
			// Attempt to Send message to Server To REGISTER USER
			Message msg = ClientUtils.sendMessage(new Message(Action.REGISTER, new String[]{FirstName,LastName,pass,Phone,EmailAddress }));
			
			
			if(!msg.isError()) {
				FnameErrorLabel.setVisible(false);
				closeScreen(null);
			} else {
				FnameErrorLabel.setText(msg.getObject() + "");
				FnameErrorLabel.setVisible(true);
			}
		}
	}
}