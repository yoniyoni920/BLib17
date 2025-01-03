package gui;

import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import base.LibraryClient;
import entities.Book;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


public class SearchBooksScreen extends AbstractScreen implements Initializable{

	@FXML
	private GridPane gridPane;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int row = 0, col = 0;
		List<Book> books = List.of(
	            new Book(0, "aa", "bb", "cc", "dd", "batman.jpg"), 
	            new Book(1, "aa1", "bb1", "cc1","dd1", "superman.jpg"),
	            new Book(2,"aa2", "bb2","cc2", "dd1", "luffy.jpg"),
	            new Book(2,"aa2", "bb2","cc2", "dd1", "spiderman.jpg"));
		for(Book book : books ) {
			try {
				 FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookCard.fxml"));
				 Node card = loader.load();
				 gridPane.setVgrow(card, Priority.ALWAYS);
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
}