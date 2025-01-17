package gui.librarian;

import base.Action;
import entities.Book;
import entities.Message;
import entities.Subscriber;
import entities.User;
import gui.AbstractScreen;
import gui.SubscriberCardScreen;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import services.ClientUtils;

import java.io.IOException;
import java.util.List;

public class LibrarianMainScreen extends AbstractScreen {
	User user;
	@FXML private Label nameTxt;
	@FXML private TextField searchSubscribers;
	@FXML private ContextMenu searchSubscribersContextMenu;
	
	public void RegisterSubscriber(ActionEvent event) throws Exception {
		RegisterViaLibrerianScreen librarianRegister = (RegisterViaLibrerianScreen)screenManager
				.openScreen("librarian/RegisterViaLibrerianScreen", "Register Screen");
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

	public void searchSubscribers(KeyEvent event) throws Exception {
		String search = searchSubscribers.getText();

		if (!search.isEmpty()) {
			Message msg = ClientUtils.sendMessage(Action.SEARCH_SUBSCRIBERS, new String[] { search, "first_name" });
			if (!msg.isError()) {
				List<Subscriber> subs = (List<Subscriber>)msg.getObject();

				ObservableList<MenuItem> list = searchSubscribersContextMenu.getItems();
				list.clear();

				for (Subscriber sub : subs) {
					MenuItem item = new MenuItem(sub.getName() + " (" + sub.getId() + ")");
					list.add(item);
					item.setUserData(sub);
				}

				if (!searchSubscribersContextMenu.isShowing()) {
					Bounds bounds = searchSubscribers.localToScreen(searchSubscribers.getBoundsInLocal());
					searchSubscribersContextMenu.show(searchSubscribers, bounds.getMinX(), bounds.getMaxY());
				}
			}
		}
	}

	public void onChoseSubscriber(ActionEvent event) throws IOException {
		SubscriberCardScreen card = (SubscriberCardScreen)screenManager.openScreen("SubscriberCardScreen", "Subscriber Card Screen");

		try {
			MenuItem item = (MenuItem)event.getTarget();
			if (item != null) {
				card.setData((Subscriber)item.getUserData(), false);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}