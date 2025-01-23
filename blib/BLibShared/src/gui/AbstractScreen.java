package gui;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/*
 * AbstractScreen is the base class for ScrenManager. 
 * help prevent code DRY-(Don't Repeat Yourself)
 */
public abstract class AbstractScreen implements Initializable {
	protected Scene scene;

	protected ScreenManager screenManager;
	
	protected String title;

	@FXML protected Label titleLabel;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public void setScreenManager(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void openScreen(Object... args) {

	}

	/**
	 * Closes the current screen and returns to the previous screen.
	 *
	 * @param event The action event triggered by the close button.
	 * @throws Exception If an error occurs during screen closing.
	 */
	public void closeScreen(ActionEvent event) {
		screenManager.closeScreen();
	}

	protected void fadeInTitle() {
		if (titleLabel == null) {
			return;
		}
		titleLabel.setOpacity(0.0); // Start with the text invisible
		// First Fade-In Transition (Welcome Message)
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), titleLabel);
		fadeIn.setFromValue(0.0); // Start fully transparent
		fadeIn.setToValue(1.0);   // Fade to fully visible
		fadeIn.setCycleCount(1);
		fadeIn.play();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fadeInTitle();
	}
}
