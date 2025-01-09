package gui.subscriber_main_screen;

import entities.Subscriber;
import gui.AbstractScreen;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class SubscriberHistoryScreen extends AbstractScreen{
	
	@FXML
	private Label welcomeText ;
	
	private Subscriber subscriber ;
	
	public void onStart(Subscriber sub) {
		fadeInLabelTransition(welcomeText);
		loadSubscriber(sub);
	}
		/*
		 * load Subscriber information to the scene
		 */
	private void loadSubscriber(Subscriber sub) {
		subscriber=sub;
	}
	
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}
	
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
