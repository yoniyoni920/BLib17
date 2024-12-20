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

public class ScreenManager {
	private Stack<AbstractScreen> screens;
	private AbstractScreen lastScreen;
	private Stage primaryStage;
	
	public ScreenManager(Stage primaryStage) {
		screens = new Stack<AbstractScreen>();
		this.primaryStage = primaryStage;
	}

	public AbstractScreen openScreen(String screenName, String title) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/" + screenName + ".fxml"));

		Parent root = loader.load();	
		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass().getResource("/gui/Main.css").toExternalForm());
		
		AbstractScreen screen = loader.getController();
		lastScreen = screen;
		screen.setScene(scene);
		screen.setScreenManager(this);
		screen.setTitle(title);
		screens.add(screen);
		
		setScreen(screen);

		return screen;
	}
	
	public void closeScreen() {
		AbstractScreen screen = screens.pop();
		AbstractScreen lastScreen = screens.lastElement();
		if (lastScreen != null) {
			setScreen(lastScreen);
		}
	}
	
	private void setScreen(AbstractScreen screen) {
		primaryStage.setTitle(screen.getTitle());
		primaryStage.setScene(screen.getScene());
		primaryStage.show();
		primaryStage.setResizable(false);
		lastScreen = screen;
	}
}
