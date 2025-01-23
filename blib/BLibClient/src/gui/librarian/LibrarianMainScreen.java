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
import javafx.scene.control.Button;
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
	@FXML private Button notificationButton;

	@Override
	public void openScreen(Object... args) {
		startUp((User)args[0]);
	}

	/**
     * This method navigates to the "Register Subscriber" screen 
     * and initializes it with the librarian's name.
     *
     * @param event The ActionEvent triggered by the UI.
     * @throws Exception If there is an error opening the screen.
     */
	public void RegisterSubscriber(ActionEvent event) throws Exception {
		screenManager.openScreen("librarian/RegisterViaLibrerianScreen", "Register Subscriber");
	}
	   /**
     * This method sets up the main screen with a welcome message 
     * that includes the librarian's name.
     *
     * @param user The user of the librarian.
     * @throws Exception If there is an error initializing the screen.
     */
	public void startUp(User user) {
		nameTxt.setText("Welcome, " + user.getName());
		this.user = user;
	}
    /**
     * This method navigates to the "Lend Book" screen.
     *
     * @throws IOException If there is an error opening the screen.
     */
	public void lendBook() throws IOException {
		screenManager.openScreen("librarian/LendBookScreen", "Lend Book");
	}

    /**
     * This method navigates to the "Report Screen".
     *
     * @param event The ActionEvent triggered by the UI.
     * @throws IOException If there is an error opening the screen.
     */
    public void openReportScreen(ActionEvent event) throws IOException {
		screenManager.openScreen("librarian/ReportScreen", "Reports");
    }

    /**
     * This method searches for subscribers based on the input in the search field.
     * It sends the search query to the server and populates the context menu with results.
     *
     * @param event The KeyEvent triggered by the user's input.
     * @throws Exception If there is an error processing the search query.
     */
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

    /**
     * This method handles the selection of a subscriber from the context menu.
     * It opens the SubscriberCardScreen and displays the selected subscriber's details.
     *
     * @param event The ActionEvent triggered by selecting a menu item.
     * @throws IOException If there is an error opening the subscriber card screen.
     */
	public void onChoseSubscriber(ActionEvent event) throws IOException {
		SubscriberCardScreen card = (SubscriberCardScreen)screenManager.openScreen("SubscriberCardScreen", "Subscriber Card");

		try {
			MenuItem item = (MenuItem)event.getTarget();
			if (item != null) {
				card.setData((Subscriber)item.getUserData(), false);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void openNotificationsScreen(ActionEvent event) throws IOException {
		NotificationsScreen screen = (NotificationsScreen) screenManager.openScreen("librarian/NotificationsScreen", "Notifications");
		screen.onStart();
	}
}