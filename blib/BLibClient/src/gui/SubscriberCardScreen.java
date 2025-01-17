package gui;

import base.Action;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
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

import java.time.format.DateTimeFormatter;
import java.util.List;

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
	@FXML private Label welcomeText;
	@FXML private VBox borrowedBooksVBox;

	private ObservableList<BookCopy> borrowedBooksObservableList;

	public void setData(Subscriber subscriber, boolean isMe) {
		this.subscriber = subscriber;

		idLabel.setText(subscriber.getId() + "");
		nameLabel.setText(subscriber.getName());
		lastNameLabel.setText(subscriber.getLastName());
		phoneLabel.setText(subscriber.getPhoneNumber());
		emailLabel.setText(subscriber.getEmail());

		boolean isFrozen = subscriber.isFrozen();
		System.out.println(isFrozen);
		frozenBox.setManaged(isFrozen);
		frozenBox.setVisible(isFrozen);

		if (isFrozen) {
			frozenText.setText("Frozen Until " + subscriber.getFrozenUntil().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
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

		fadeInLabelTransition(welcomeText);

		screenManager.getPrimaryStage().sizeToScene();
	}

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
				changeDurationBtn.setOnAction(event -> onChangeDurationBookPressed(copies.get(getIndex())));
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
		//TODO: implement returning books
	}

	private void onChangeDurationBookPressed(BookCopy bookCopy) {
		//TODO: implement changing duration of borrowing
	}

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
			Message msg = ClientUtils.sendMessage(Action.MARK_BOOK_COPY_AS_LOST, bookCopy.getCopyId());
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


	public void updateBookCopyReturnDate() {
		// TODO - implement SubscriberCardScreen.updateBookCopyReturnDate
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs a fade-in animation on the welcome label.
	 * This method animates the opacity of the welcome text from 0 (invisible) to 1 (fully visible).
	 *
	 * @param welcomeText The label to apply the fade-in transition on.
	 */
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