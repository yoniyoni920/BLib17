package gui;

import javafx.scene.Scene;

public abstract class AbstractScreen {
	protected Scene scene;

	protected ScreenManager screenManager;
	
	protected String title;
	
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
}
