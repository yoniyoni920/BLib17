package gui.librarian;

import entities.Book;
import entities.BookCopy;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
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

	public void setSubscriber(Subscriber subscriber) {
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

		borrowedBooks.setItems(FXCollections.observableList(subscriber.getBorrowedBooks()));

		TableColumn<BookCopy, String> idColumn = new TableColumn<>("Id");
		idColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.1));
		idColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBookId() + ""));

		TableColumn<BookCopy, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getBook().getTitle() + ""));
		titleColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.4));
		borrowedBooks.getColumns().addAll(idColumn, titleColumn, getActionColumn(subscriber));
	}

	private TableColumn<BookCopy, Void> getActionColumn(Subscriber subscriber) {
		List<BookCopy> copies = subscriber.getBorrowedBooks();

		TableColumn<BookCopy, Void> actionColumn = new TableColumn<>("Action");
		actionColumn.prefWidthProperty().bind(borrowedBooks.widthProperty().multiply(0.45));

		actionColumn.setCellFactory(col -> new TableCell<BookCopy, Void>() {
			private final Button returnBtn = new Button("Return");
			{
				returnBtn.setOnAction(event -> onReturnBookPressed(copies.get(getIndex())));
			}

			private final Button changeDurationBtn = new Button("Change Borrow Duration");
			{
				changeDurationBtn.setOnAction(event -> onChangeDurationBookPressed(copies.get(getIndex())));
			}

			@Override
            protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					HBox hBox = new HBox(returnBtn, changeDurationBtn);
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

	private ListCell<BookCopy> createListCell() {
		return new ListCell<BookCopy>() {
			@Override
			protected void updateItem(BookCopy bookCopy, boolean empty) {
				super.updateItem(bookCopy, empty);
				if (!empty) {
					//				setGraphic(bookCopy.getBook().getImage());
					setText(bookCopy.getBook().getTitle() + " By " + bookCopy.getBook().getAuthors());
				}
			}
		};
	}

	public void updateBookCopyReturnDate() {
		// TODO - implement SubscriberCardScreen.updateBookCopyReturnDate
		throw new UnsupportedOperationException();
	}

}