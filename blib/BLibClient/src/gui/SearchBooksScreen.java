package gui;

import java.io.IOException;
import java.net.URL;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import base.Action;
import base.ClientApplication;
import base.LibraryClient;
import entities.Book;
import entities.Message;
import entities.Subscriber;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.FormatStringConverter;
import services.ClientUtils;


public class SearchBooksScreen extends AbstractScreen implements Initializable{
	
	ObservableList<String> choiceBoxList = FXCollections.observableArrayList("title", "genre", "description");
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private TextField searchId;
	
	@FXML
	private ChoiceBox<String> choiceBox;
	
	@FXML
	private Text textId;

	@FXML
	private ScrollPane scrollPane;

	private Timer timer;
	private TimerTask searchTask;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		timer = new Timer();

		choiceBox.setItems(choiceBoxList);
		choiceBox.setValue("title");

		// Convert the values to be capitalized rather than lowercase
		choiceBox.setConverter(new StringConverter<String>() {
			@Override
			public String fromString(String str) {
				return str.toLowerCase();
			}

			@Override
			public String toString(String str) {
				return  str.substring(0, 1).toUpperCase() + str.substring(1);
			}
		});
		searchBooks();

		// Lets you speed up the scroll speed
		final double SCROLL_SPEED = 0.014;
		scrollPane.getContent().setOnScroll(scrollEvent -> {
			double deltaY = scrollEvent.getDeltaY() * SCROLL_SPEED;
			scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
		});
	}

	public void onSearch(InputEvent event) throws Exception {
		// Makes search faster
		if (searchTask != null) {
			searchTask.cancel();
		}
		searchTask = new TimerTask() {
			@Override
			public void run() {
				searchBooks();
			}
		};
		timer.schedule(searchTask, 200);
	}

	public void searchBooks() {
		String[] searchInfo = {searchId.getText(), choiceBox.getValue()};

		ClientUtils.sendMessage(new Message(Action.SEARCH_BOOKS, searchInfo), msg -> {
			textId.setVisible(false);

			int definedColumns = 4;
			int row = 0, col = 0;

			// Clear previous row constraints
			gridPane.getChildren().clear();
			gridPane.getRowConstraints().clear();
			gridPane.getColumnConstraints().clear();

			System.out.println("Received object type: " + msg.getObject().getClass().getName());
			List<Book> books = (List<Book>) msg.getObject();
			if(!books.isEmpty()) {
				RowConstraints rc = new RowConstraints();
				double rowCount = Math.ceil((double)books.size()/definedColumns);
				rc.setPercentHeight(100 / rowCount);

				for (int i = 0; i < rowCount; i++) {
					gridPane.getRowConstraints().add(rc);
				}

				ColumnConstraints cc = new ColumnConstraints();
				cc.setPercentWidth(100 / definedColumns);

				for (int i = 0; i < definedColumns; i++) {
					gridPane.getColumnConstraints().add(cc);
				}

				for(Book book : books) {
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookCard.fxml"));
						Node card = loader.load();
						GridPane.setVgrow(card, Priority.ALWAYS);
						BookCard bookCard = loader.getController();
						bookCard.setBookData(book);
						gridPane.add(card, col, row);
						col++;
						if (col == definedColumns) { // definedColumns cards per row
							col = 0;
							row++;
						}
					} catch(IOException e) {
						e.printStackTrace();
					}
				}
			}
			else {
				textId.setVisible(true);
			}
		});
	}
}