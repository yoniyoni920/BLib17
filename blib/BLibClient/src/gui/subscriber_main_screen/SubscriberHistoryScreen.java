package gui.subscriber_main_screen;

import entities.HistoryEntry;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import services.InterfaceUtils;

import java.time.format.DateTimeFormatter;

public class SubscriberHistoryScreen extends AbstractScreen{

	public TableColumn<HistoryEntry, String> dateColumn;
	public TableColumn<HistoryEntry, String> detailsColumn;
	@FXML private Label welcomeText;
	@FXML private TableView<HistoryEntry> historyTable;


	private Subscriber subscriber ;
	
	public void onStart(Subscriber sub) {
		fadeInLabelTransition(welcomeText);
		loadSubscriber(sub);
	}
		/*
		 * load Subscriber information to the scene
		 */
	private void loadSubscriber(Subscriber sub) {
		subscriber = sub;
		historyTable.setItems(FXCollections.observableArrayList(sub.getHistory()));

		dateColumn.setCellValueFactory(cellData ->
			new SimpleStringProperty(InterfaceUtils.formatDate(cellData.getValue().getDate())
		));
		detailsColumn.setCellValueFactory(cellData -> {
			HistoryEntry item = cellData.getValue();
			String action = item.getAction();
			String formattedAction;

			if (action.equals("lost")) {
				formattedAction = String.format("Lost book named: %s", item.getBookName());
			} else if (action.equals("borrow")) {
				formattedAction = String.format("Borrowed book named: %s", item.getBookName());
			} else if (action.equals("late")) {
				formattedAction = String.format("Late for book return. Book name: %s", item.getBookName());
			} else if (action.equals("freeze")) {
				formattedAction = "Got frozen for 30 days for not returning a book for more than " +
						"a week after the return date.";
			} else if (action.equals("return")) {
				formattedAction = String.format("Returned book named: %s", item.getBookName());
			} else if (action.equals("login")) {
				formattedAction = "Logged in";
			} else if (action.equals("extend_by_subscriber")) {
				formattedAction = String.format("Extended borrowing duration for book named: %s", item.getBookName());
			} else if (action.equals("extend_by_librarian")) {
				formattedAction = String.format("Librarian %s extended borrowing duration for book named: %s",
						item.getLibrarianName(), item.getBookName());
			} else if (action.equals("order")) {
				formattedAction = String.format("Ordered book named: %s", item.getBookName());
			} else {
				formattedAction = "Unknown action: " + action;
			}
			return new SimpleStringProperty(formattedAction);
		});
	}

	private void fadeInLabelTransition(Label welcomeText) {
		welcomeText.setOpacity(0.0); // Start with the text invisible

	    // First Fade-In Transition (Welcome Message)
	    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), welcomeText);
	    fadeIn.setFromValue(0.0); // Start fully transparent
	    fadeIn.setToValue(1.0);   // Fade to fully visible
	    fadeIn.setCycleCount(1);
	    fadeIn.play();
	}
}
