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
    /**
     * This method navigates to the "Register Subscriber" screen 
     * and initializes it with the librarian's name.
     *
     * @param event The ActionEvent triggered by the UI.
     * @throws Exception If there is an error opening the screen.
     */
	public void RegisterSubscriber(ActionEvent event) throws Exception {
		RegisterViaLibrerianScreen librarianRegister = (RegisterViaLibrerianScreen)screenManager
				.openScreen("librarian/RegisterViaLibrerianScreen", "Register Screen");
		librarianRegister.startUp(user.getName());
	}
	   /**
     * This method sets up the main screen with a welcome message 
     * that includes the librarian's name.
     *
     * @param name The name of the librarian.
     * @throws Exception If there is an error initializing the screen.
     */
	public void startUp(String name) throws Exception {
		nameTxt.setText("Welcome, " + name);
	}
    /**
     * This method navigates to the "Lend Book" screen.
     *
     * @throws IOException If there is an error opening the screen.
     */
	public void lendBook() throws IOException {
		screenManager.openScreen("librarian/LendBookScreen", "Reports");
	}
    /**
     * This method is a placeholder for getting member status functionality.
     */
	public void getMemberStatus() {
		// TODO - implement LibrarianMainScreen.getMemberStatus
		throw new UnsupportedOperationException();
	}
	  /**
     * This method is a placeholder for returning books functionality.
     */
	public void returnBook() {
		// TODO - implement LibrarianMainScreen.returnBook
		throw new UnsupportedOperationException();
	}
    /**
     * This method sets the logged-in user's details for the User.
     *
     * @param user The logged-in user object.
     */
	public void loadUser(User user) {
		this.user = user;
	}

	/*
	 * close Window and returns to previous screen
	 */
	public void closeWindow(ActionEvent event) throws Exception {
		screenManager.closeScreen();
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