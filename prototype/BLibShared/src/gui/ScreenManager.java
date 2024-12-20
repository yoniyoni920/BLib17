package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * Base code for presenting screen and controlling FXML files.
 * Helps keep the code DRY.
 */


public class ScreenManager {
	private Stack<AbstractScreen> screens;
	private Stage primaryStage;
	
	public ScreenManager(Stage primaryStage) {
		screens = new Stack<AbstractScreen>();
		this.primaryStage = primaryStage;
	}

	/**
	 * Receives screenName of a screen (gui/{screenName}.fxml), loads it and returns the AbstractScreen associated with it
	 * AbstractScreen is basically a JavaFX controller
	 * 
	 * @param screenName
	 * @param title
	 * @return
	 * @throws IOException
	 */
	public AbstractScreen openScreen(String screenName, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/" + screenName + ".fxml"));
		Parent root = loader.load();	
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Main.css").toExternalForm());
		AbstractScreen screen = loader.getController();
		screen.setScene(scene);
		screen.setScreenManager(this);
		screen.setTitle(title);
		screens.add(screen);
		
		setScreen(screen);

		return screen;
	}
	
	/**
	 * Closes the screen and returns the previous screen that was open
	 * NOTE: if the last screen contained information it will still be loaded.
	 * You must handle this.
	 * 
	 * @return AbstractScreen
	 */
	public AbstractScreen closeScreen() {
		screens.pop(); // Pops the last screen
		AbstractScreen lastScreen = screens.lastElement();
		if (lastScreen != null) {
			setScreen(lastScreen);
		}
		
		return lastScreen;
	}
	
	/**
	 * Sets the screen. This is internally used.
	 * @param screen
	 */
	private void setScreen(AbstractScreen screen) {
		primaryStage.setTitle(screen.getTitle());
		primaryStage.setScene(screen.getScene());
		primaryStage.show();
		primaryStage.setResizable(false);
	}
}
