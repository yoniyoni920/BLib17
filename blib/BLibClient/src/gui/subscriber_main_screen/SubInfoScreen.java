package gui.subscriber_main_screen;

import entities.Subscriber;
import gui.AbstractScreen;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Controller for the Subscriber Info Screen.
 * This class displays the subscriber's information such as ID, name, phone, email, and status.
 * It allows the user to navigate to the Subscriber Settings screen and close the current window.
 * 
 * @author Helal Hammoud
 */
public class SubInfoScreen extends AbstractScreen {
    
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
    
    @FXML
    private Label welcomeText;
    
    @FXML 
    private Label statusLabel;
    
    ObservableList<String> list;
    

    /**
     * Initializes the screen by loading and displaying the subscriber's information.
     * This method is called when the screen is loaded and the subscriber's data is passed in.
     *
     * @param sub The subscriber whose information will be displayed on the screen.
     */
    public void onStart(Subscriber sub) {
        loadData(sub);
        renderData();
    }

    /**
     * Loads the subscriber's information into the `subscriber` object.
     * This method prepares the data to be rendered on the screen.
     *
     * @param sub The subscriber whose information will be loaded.
     */
    public void loadData(Subscriber sub) {
        this.subscriber = sub;
    }

    /**
     * Renders the subscriber's data into the corresponding UI labels.
     * Displays subscriber's ID, name, phone, email, and status.
     */
    private void renderData() {
        idLabel.setText(subscriber.getId() + "");
        nameLabel.setText(subscriber.getName() + " " + subscriber.getLastName());
        // historyLabel.setText("" + sub.getDetailedSubscriptionHistory());
        phoneLabel.setText(subscriber.getPhoneNumber());
        emailLabel.setText(subscriber.getEmail());
        statusLabel.setText(subscriber.getStatus());
        fadeInLabelTransition(welcomeText);
    }

    /**
     * Closes the current window and returns to the previous screen.
     * 
     * @param event The action event triggered by the close window action.
     * @throws Exception If an error occurs while closing the screen.
     */
    public void closeWindow(ActionEvent event) throws Exception {
        screenManager.closeScreen();
    }

    /**
     * Opens the Subscriber Settings screen and loads the necessary subscriber information into it.
     * This allows the subscriber to edit their information.
     *
     * @param event The action event triggered by the open configure screen action.
     * @throws Exception If an error occurs while opening the Subscriber Settings screen.
     */
    public void openConfigureScreen(ActionEvent event) throws Exception {
        SubscriberSettingsScreen screen = (SubscriberSettingsScreen) screenManager.openScreen("subscriber_main_screen/SubscriberSettingsScreen", "Subscriber Settings");
        screen.onStart(subscriber);
    }

    /**
     * Performs a fade-in animation on the welcome label.
     * This method animates the opacity of the welcome text from 0 (invisible) to 1 (fully visible).
     *
     * @param welcomeText The label to apply the fade-in transition on.
     */
    private void fadeInLabelTransition(Label welcomeText) {
        welcomeText.setOpacity(0.0); // Start with the text invisible

        // First Fade-In Transition (Welcome Message)
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
        fadeIn.setFromValue(0.0); // Start fully transparent
        fadeIn.setToValue(1.0);   // Fade to fully visible
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }
}
