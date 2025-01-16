package gui.librarian;

import base.Action;
import entities.Message;
import entities.Subscriber;
import entities.User;
import gui.AbstractScreen;
import gui.SubscriberCardScreen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.ClientUtils;

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
		screenManager.openScreen("librarian/LendBookScreen", "Reports");
	}

	public void getMemberStatus() {
		// TODO - implement LibrarianMainScreen.getMemberStatus
		throw new UnsupportedOperationException();
	}

	public void returnBook() {
		// TODO - implement LibrarianMainScreen.returnBook
		throw new UnsupportedOperationException();
	}

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
		screenManager.openScreen("librarian/ReportScreen", "Reports");
    }

	public void searchSubscribers(ActionEvent event) throws Exception {
		SubscriberCardScreen card = (SubscriberCardScreen)screenManager.openScreen("SubscriberCardScreen", "Subscriber Card Screen");
		Message sub = ClientUtils.sendMessage(Action.GET_SUBSCRIBER_BY_ID, 1); // TODO: implement searching for real

		try {
			card.setData((Subscriber)sub.getObject(), false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}