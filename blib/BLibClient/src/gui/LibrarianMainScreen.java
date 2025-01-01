package gui;

import entities.User;
import javafx.event.ActionEvent;

public class LibrarianMainScreen extends AbstractScreen {

	public void registerSubscriber() {
		// TODO - implement LibrarianMainScreen.registerSubscriber
		throw new UnsupportedOperationException();
	}

	public void lendBook() {
		// TODO - implement LibrarianMainScreen.lendBook
		throw new UnsupportedOperationException();
	}

	public void getMemberStatus() {
		// TODO - implement LibrarianMainScreen.getMemberStatus
		throw new UnsupportedOperationException();
	}

	public void returnBook() {
		// TODO - implement LibrarianMainScreen.returnBook
		throw new UnsupportedOperationException();
	}

	//TODO: implement
	public void loadUser(User user) {

	}

	/*
	 * close Window and returns to previous screen
	 */
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}
}