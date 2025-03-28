package gui;

import java.io.IOException;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
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
	 * @param screenName the name of the screen (gui/{screenName}.fxml)
	 * @param title The title of the screen
	 * @param args Optional argument for .openScreen
	 * @return AbstractScreen of the controller object of the screen
	 * @throws IOException
	 */
	public AbstractScreen openScreen(String screenName, String title, Object... args) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/" + screenName + ".fxml"));
		Parent root = loader.load();	
		Scene scene = new Scene(root);
		AbstractScreen screen = loader.getController();
		screen.setScreenManager(this);
		screen.openScreen(args);
		screen.setScene(scene);
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
		primaryStage.sizeToScene();
	}

	/**
	 * @return The current primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
