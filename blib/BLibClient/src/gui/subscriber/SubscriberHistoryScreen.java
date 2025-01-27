package gui.subscriber;

import entities.HistoryAction;
import entities.HistoryEntry;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import services.InterfaceUtils;

/**
 * A screen that shows the history of a single subscriber
 */
public class SubscriberHistoryScreen extends AbstractScreen {
	public TableColumn<HistoryEntry, String> dateColumn;
	public TableColumn<HistoryEntry, String> detailsColumn;
	@FXML private TableView<HistoryEntry> historyTable;

	private Subscriber subscriber;

	@Override
	public void openScreen(Object... args) {
		loadSubscriber((Subscriber)args[0]);
	}

	/**
	 * Load the subscriber's info
	 * @param sub The subscriber to work with
	 */
	private void loadSubscriber(Subscriber sub) {
		subscriber = sub;
		historyTable.setItems(FXCollections.observableArrayList(sub.getHistory()));

		dateColumn.setCellValueFactory(cellData ->
			new SimpleStringProperty(InterfaceUtils.formatDate(cellData.getValue().getDate())
		));
		detailsColumn.setCellValueFactory(cellData -> {
			HistoryEntry item = cellData.getValue();
			HistoryAction action = item.getAction();
			String formattedAction;

			titleLabel.setText(String.format("%s's History", sub.getName()));

			switch (action) {
				case LOST_BOOK:
					formattedAction = String.format("Lost book named: %s", item.getBookName());
					break;
				case BORROW_BOOK:
					formattedAction = String.format("Borrowed book named: %s", item.getBookName());
					break;
				case LATE_RETURN:
					formattedAction = String.format("Late for book return. Book name: %s", item.getBookName());
					break;
				case FREEZE_SUBSCRIBER:
					formattedAction = "Got frozen for 30 days for not returning a book for more than " +
							"a week after the return date.";
					break;
				case RETURN_BOOK:
					formattedAction = String.format("Returned book named: %s", item.getBookName());
					break;
				case LOGIN_SUBSCRIBER:
					formattedAction = "Logged in";
					break;
				case EXTEND_BORROW_SUBSCRIBER:
					formattedAction = String.format("Extended borrowing duration for book named: %s", item.getBookName());
					break;
				case EXTEND_BORROW_LIBRARIAN:
					formattedAction = String.format("Librarian %s extended borrowing duration for book named: %s",
							item.getLibrarianName(), item.getBookName());
					break;
				case ORDER_BOOK:
					formattedAction = String.format("Ordered book named: %s", item.getBookName());
					break;
				default:
					formattedAction = "Unknown action: " + action;
					break;
			}
			return new SimpleStringProperty(formattedAction);
		});
	}
}
