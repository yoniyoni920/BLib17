package gui.librarian;

import entities.User;
import gui.AbstractScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class LibrarianMainScreen extends AbstractScreen {
	User user;
	@FXML
	private Label nameTxt;
	
	
	public void RegisterSubscriber(ActionEvent event) throws Exception {
		RegisterViaLibrerianScreen librarianRegister =  (RegisterViaLibrerianScreen)screenManager.openScreen("RegisterViaLibrerianScreen", "Register Screen");
		librarianRegister.startUp(user.getName());
	}
	public void startUp(String name) throws Exception {
		nameTxt.setText("Welcome, " + name);
	}
	public void lendBook() throws IOException {
		screenManager.openScreen("LendBookScreen", "Reports");
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
		this.user = user;
	}

	/*
	 * close Window and returns to previous screen
	 */
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
	}

    public void openReportScreen(ActionEvent event) throws IOException {
		screenManager.openScreen("ReportScreen", "Reports");
    }
}