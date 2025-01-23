package gui;

import base.Action;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import gui.librarian.ExtendBorrowTimeScreen;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import services.ClientUtils;
import services.InterfaceUtils;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * Controller class for the SubscriberCard screen.
 * This class handles the display of subscriber details, their borrowed books,
 * and actions that can be performed on the subscriber or their borrowed books.
 */
public class SubscriberCardScreen extends AbstractScreen {
	private Subscriber subscriber;

	@FXML private Label idLabel;
	@FXML private Label nameLabel;
	@FXML private Label lastNameLabel;
	@FXML private Label phoneLabel;
	@FXML private Label emailLabel;
	@FXML private Label frozenText;
	@FXML private HBox frozenBox;
	@FXML private TableView<BookCopy> borrowedBooks;
	@FXML private VBox borrowedBooksVBox;

	private ObservableList<BookCopy> borrowedBooksObservableList;
    /**
     * Sets the subscriber data and configures the UI accordingly.
     *
     * @param subscriber The subscriber object containing all relevant data.
     * @param isMe Indicates if the subscriber is the logged-in user.
     */
	public void setData(Subscriber subscriber, boolean isMe) {
		this.subscriber = subscriber;

		idLabel.setText(subscriber.getId() + "");
		nameLabel.setText(subscriber.getName());
		lastNameLabel.setText(subscriber.getLastName());
		phoneLabel.setText(subscriber.getPhoneNumber());
		emailLabel.setText(subscriber.getEmail());

		boolean isFrozen = subscriber.isFrozen();
		frozenBox.setManaged(isFrozen);
		frozenBox.setVisible(isFrozen);
		if (isFrozen) {
			frozenText.setText("Frozen Until " + InterfaceUtils.formatDate(subscriber.getFrozenUntil()));
		}

		if (!isMe) {
			borrowedBooksObservableList = FXCollections.observableList(subscriber.getBorrowedBooks());
			borrowedBooks.setItems(borrowedBooksObservableList);

			TableColumn<BookCopy, String> idColumn = new TableColumn<>("Id");
			idColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.05));
			idColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBookId() + ""));

			TableColumn<BookCopy, String> titleColumn = new TableColumn<>("Title");
			titleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBook().getTitle() + ""));
			titleColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.35));

			borrowedBooks.getColumns().clear();
			borrowedBooks.getColumns().addAll(idColumn, titleColumn, getActionColumn(subscriber));

			borrowedBooksVBox.setPrefHeight(300);
		} else {
			borrowedBooksVBox.setPrefHeight(0);
		}

		borrowedBooksVBox.setVisible(!isMe);
		borrowedBooksVBox.setManaged(!isMe);

		screenManager.getPrimaryStage().sizeToScene();
	}
    /**
     * Configures the action column for the borrowed books table.
     *
     * @param subscriber The subscriber object to which the books belong.
     * @return The configured action column.
     */
	private TableColumn<BookCopy, Void> getActionColumn(Subscriber subscriber) {
		List<BookCopy> copies = subscriber.getBorrowedBooks();

		TableColumn<BookCopy, Void> actionColumn = new TableColumn<>("Action");
		actionColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.55));

		actionColumn.setCellFactory(col -> new TableCell<BookCopy, Void>() {
			private final Button returnBtn = new Button("Return");
			{
				returnBtn.setOnAction(event -> onReturnBookPressed(copies.get(getIndex())));
			}

			private final Button changeDurationBtn = new Button("Change Borrow Duration");
			{
				changeDurationBtn.setOnAction(event -> {
					try {
						onChangeDurationBookPressed(copies.get(getIndex()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
			}

			private final Button markAsLost = new Button("Mark as Lost");
			{
				markAsLost.setOnAction(event -> onMarkBookAsLostPressed(copies.get(getIndex())));
			}

			@Override
            protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					HBox hBox = new HBox(returnBtn, changeDurationBtn, markAsLost);
					hBox.setSpacing(8);
					hBox.setPadding(new Insets(8, 0, 8, 0));
					setGraphic(hBox);
				}
			}
		});
		return actionColumn;
	}

	private void onReturnBookPressed(BookCopy bookCopy) {
		int bookCopyId = bookCopy.getId();
		ClientUtils.sendMessage(new Message(Action.RETURN_BOOK, bookCopyId));
		List<BookCopy> copies = subscriber.getBorrowedBooks();
		copies.remove(bookCopy);
		borrowedBooks.setItems(borrowedBooksObservableList);
		borrowedBooks.refresh();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Returned Book Successfully");
		alert.setHeaderText("The book returned successfully");
		alert.showAndWait();
	}

	private void onChangeDurationBookPressed(BookCopy bookCopy) throws IOException {
		ExtendBorrowTimeScreen screen = (ExtendBorrowTimeScreen) screenManager
				.openScreen("librarian/ExtendBorrowTimeScreen", "Extend Borrow Duration");
		screen.onStart(bookCopy);
	}
	   /**
     * Handles the action for marking a book as lost.
     * @param bookCopy The book copy to be marked as lost.
     */
	private void onMarkBookAsLostPressed(BookCopy bookCopy) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Mark Book as Lost");
		alert.setHeaderText("Are you sure?");
		alert.setContentText("Marking the book copy as lost does the following extra things:\n" +
			"1. Removes the book from the subscriber and cancels an order, if exists.\n" +
			"2. Freezes the subscriber's account for 30 days.\n" +
			"3. Logs the offense in their history."
		);

		alert.showAndWait();

		if (alert.getResult() == ButtonType.OK) {
			Message msg = ClientUtils.sendMessage(Action.MARK_BOOK_COPY_AS_LOST, bookCopy.getId());
			if (msg.getObject().equals(true)) {
				Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
				successAlert.setTitle("Mark Book as Lost");
				successAlert.setHeaderText("Successfully marked book as lost!");
				successAlert.showAndWait();
				Message updateSubscriberMsg = ClientUtils.sendMessage(Action.GET_SUBSCRIBER_BY_ID, subscriber.getId());
				if (!updateSubscriberMsg.isError()) {
					setData((Subscriber)updateSubscriberMsg.getObject(), false);
				}
			} else {
				Alert erorrAlert = new Alert(Alert.AlertType.ERROR);
				erorrAlert.setTitle("Mark Book as Lost");
				erorrAlert.setHeaderText("Failed to mark book as lost!");
				erorrAlert.setContentText("Perhaps it was already marked as lost?");
				erorrAlert.showAndWait();
			}
		}
	}
}