package gui;

import entities.Subscriber;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/*
 * controller for SubscriberMainScreen.fxml
 */
public class SubscriberMainScreen extends AbstractScreen {
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
		/*
		 * load Subscriber information to the scene
		 */
	public void loadSubscriber(Subscriber sub) {
		subscriber=sub;
		idLabel.setText(sub.getId());
		nameLabel.setText(sub.getFirstName());
//		historyLabel.setText("" +sub.getDetailedSubscriptionHistory());
		phoneLabel.setText(sub.getPhoneNumber());
		emailLabel.setText(sub.getEmail());
	}
	/*
	 * close Window and returns to previous screen
	 */
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}
	/*
	 * opens Configure Screen and loads needed information to it.
	 */
	public void openConfigureScreen(ActionEvent event) throws Exception {
		SubscriberSettingsScreen screen = (SubscriberSettingsScreen)screenManager.openScreen("SubscriberSettingsScreen","Subscriber Settings");
		screen.loadSubscriber(subscriber);
	}
}
