package gui;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class SearchBooksScreen extends AbstractScreen implements Initializable{
	
	ObservableList<String> choiceBoxList = FXCollections.observableArrayList("title", "genre", "description");
	
	@FXML
	private GridPane gridPane;
	
	@FXML
	private TextField searchId;
	
	@FXML
	private ChoiceBox choiceBox;
	
	@FXML
	private Text textId;
	
	public void onSearch(ActionEvent event) throws Exception{
		searchBooks();
	}
	public void searchBooks() {
		int row = 0, col = 0;
		textId.setVisible(false);
		String[] searchInfo = {searchId.getText() ,(String)choiceBox.getValue()};
		gridPane.getChildren().clear();
		gridPane.getRowConstraints().clear(); // Clear previous row constraints
	    gridPane.getColumnConstraints().clear();
		Message msg = ClientApplication.chat.sendToServer(new Message(Action.SEARCH_BOOKS, searchInfo));
		System.out.println("Received object type: " + msg.getObject().getClass().getName());
		List<Book> books =  (List<Book>) msg.getObject();
		if(!books.isEmpty()) {
			RowConstraints rc = new RowConstraints();
			double rowCount = Math.ceil((double)books.size()/3);
			rc.setPercentHeight(100 / rowCount);

			for (int i = 0; i < rowCount; i++) {
		    	gridPane.getRowConstraints().add(rc);
			}

			ColumnConstraints cc = new ColumnConstraints();
				cc.setPercentWidth(100d / 3);
		
			for (int i = 0; i < 3; i++) {
		    	gridPane.getColumnConstraints().add(cc);
			}
			for(Book book : books ) {
				try {
				 	FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookCard.fxml"));
				 	Node card = loader.load();
				 	GridPane.setVgrow(card, Priority.ALWAYS);
				 	BookCard bookCard = loader.getController();
				 	bookCard.setBookData(book);
				 	gridPane.add(card, col, row);
				 	col++;
				 	if (col == 3) { // 3 cards per row
	                    	col = 0;
	                    	row++;

	                }
				}catch(IOException e) {
                	e.printStackTrace();
            	}
			}
		}
		else {
			textId.setVisible(true);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		choiceBox.setItems(choiceBoxList);
		choiceBox.setValue("title");
		searchBooks();
	}
}