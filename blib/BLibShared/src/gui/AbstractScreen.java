package gui;

import javafx.scene.Scene;
/*
 * AbstractScreen is the base class for ScrenManager. 
 * help prevent code DRY-(Don't Repeat Yourself)
 */
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
