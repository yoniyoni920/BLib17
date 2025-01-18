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
import services.InterfaceUtils;

/**
 * Controller class for the "SearchBooksScreen.fxml" view.
 * Handles the logic for searching and displaying books in a grid layout.
 */
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
	/**
     * Initializes the screen with default values and configurations.
     *
     * @param arg0 The URL location used to resolve relative paths.
     * @param arg1 The resource bundle for internationalization.
     */
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
    /**
     * Handles user input in the search field and triggers a debounced search.
     *
     * @param event The input event from the search field.
     * @throws Exception If the search process encounters an error.
     */
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


	
	  /**
     * Searches for books based on the user's input and selected filter.
     * Updates the grid with the resulting books or shows a "no results" message.
     */
	public void searchBooks() {
		String[] searchInfo = {searchId.getText(), choiceBox.getValue()};

		ClientUtils.sendMessage(new Message(Action.SEARCH_BOOKS, searchInfo), msg -> {
			List<Book> books = (List<Book>)msg.getObject();
			textId.setVisible(false);

			gridPane.getChildren().clear();
			if(!books.isEmpty()) {
				InterfaceUtils.makeGrid(gridPane, 4, books, book -> {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookCard.fxml"));
                    try {
						Node card = loader.load();
						BookCard bookCard = loader.getController();
						bookCard.setBookData(book);
						return card;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
				});
			}
			else {
				textId.setVisible(true);
			}
		});
	}
}