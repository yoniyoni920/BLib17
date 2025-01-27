package gui.subscriber;

import java.util.Arrays;
import java.util.List;
import base.Action;
import entities.Message;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import services.ClientUtils;

/**
 * Controller for the Subscriber Settings Screen.
 * This class handles the interactions on the subscriber settings screen, allowing users to update their personal information.
 * It includes methods for validating inputs, updating subscriber information, and transitioning between screens.
 * 
 * @author Helal Hammoud
 */
public class SubscriberSettingsScreen extends AbstractScreen {
    @FXML
    private TextField phoneTxtField;
    
    @FXML
    private TextField emailTxtField;
    
    @FXML
    private TextField firstNameTxtField;
    
    @FXML
    private TextField lastNameTxtField;

    @FXML
    private PasswordField oldPasswordTxtField;
    
    @FXML
    private PasswordField newPasswordTxtField;
    
    @FXML 
    private Label alertMessage;

    private Subscriber sub;

    /**
     * Initializes the screen with the subscriber's information.
     * This method is called when the screen is loaded and the subscriber information is passed in.
     */
    @Override
    public void openScreen(Object... args) {
        this.sub = (Subscriber)args[0];
        renderData();
    }

    /**
     * Renders the subscriber's data into the corresponding UI fields.
     */
    private void renderData() {
        firstNameTxtField.setText(sub.getName());
        lastNameTxtField.setText(sub.getLastName());
        phoneTxtField.setText(sub.getPhoneNumber());
        emailTxtField.setText(sub.getEmail());
    }

    /**
     * Updates the subscriber's information.
     * Validates the input, checks for changes, and sends the updated information to the server for processing.
     * 
     * @param event The action event triggered by clicking the update button.
     * @throws Exception If an error occurs while sending the data or updating the information.
     */
    public void updateInfo(ActionEvent event) throws Exception {
        // Check for empty fields or invalid inputs
        if (hasEmptyFields()) {
            alertMessage.setOpacity(1);
            return;
        }

        // Check if no changes were made
        if (hasNoChanges()) {
            System.out.println("no changes");
            closeScreen(null);
            return;
        }

        // Construct the updated info list
        List<String> changedInfo = Arrays.asList(
            sub.getId() + "",
            trimSpaces(firstNameTxtField.getText()),
            trimSpaces(lastNameTxtField.getText()),
            trimSpaces(phoneTxtField.getText()),
            trimSpaces(emailTxtField.getText()),
            trimSpaces(newPasswordTxtField.getText()).isEmpty() ? sub.getPassword() : trimSpaces(newPasswordTxtField.getText())
        );

        // Send the changes to the server
        Message msg = ClientUtils.sendMessage(new Message(Action.UPDATE_SUBSCRIBER, changedInfo));

        // Update in-memory data if the server update is successful
        if (!msg.isError()) {
            sub.setName(changedInfo.get(1));
            sub.setLastName(changedInfo.get(2));
            sub.setPhoneNumber(changedInfo.get(3));
            sub.setEmail(changedInfo.get(4));
            sub.setPassword(changedInfo.get(5));
        }

        closeScreen(null);
    }

    /**
     * Utility method to remove spaces from a string used in updateInfo().
     *
     * @param input The input string to be trimmed of spaces.
     * @return The input string with spaces removed.
     */
    private String trimSpaces(String input) {
        return input == null ? "" : input.replaceAll(" ", "");
    }

    /**
     * Validation for empty fields used in updateInfo().
     * Ensures that no field is left empty or contains invalid data.
     *
     * @return true if any required fields are empty or invalid, false otherwise.
     */
    private boolean hasEmptyFields() {
        return firstNameTxtField.getText().isEmpty() ||
               lastNameTxtField.getText().isEmpty() ||
               phoneTxtField.getText().isEmpty() ||
               emailTxtField.getText().isEmpty() ||
               (oldPasswordTxtField.getText().isEmpty() && !newPasswordTxtField.getText().isEmpty()) ||
               (!oldPasswordTxtField.getText().isEmpty() && newPasswordTxtField.getText().isEmpty()) ||
               ((!oldPasswordTxtField.getText().isEmpty() && !newPasswordTxtField.getText().isEmpty()) &&
                !oldPasswordTxtField.getText().equals(sub.getPassword()));
    }

    /**
     * Checks if no changes were made to the subscriber's information.
     *
     * @return true if no changes were made, false otherwise.
     */
    private boolean hasNoChanges() {
        return trimSpaces(firstNameTxtField.getText()).equals(sub.getName()) &&
               trimSpaces(lastNameTxtField.getText()).equals(sub.getLastName()) &&
               trimSpaces(phoneTxtField.getText()).equals(sub.getPhoneNumber()) &&
               trimSpaces(emailTxtField.getText()).equals(sub.getEmail()) &&
               ((trimSpaces(oldPasswordTxtField.getText()).isEmpty() && trimSpaces(newPasswordTxtField.getText()).isEmpty()) ||
                (!trimSpaces(oldPasswordTxtField.getText()).isEmpty() && 
                  trimSpaces(newPasswordTxtField.getText()).equals(sub.getPassword())));
    }
}

