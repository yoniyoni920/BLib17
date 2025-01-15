package gui;

import java.net.URISyntaxException;

import entities.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class BookCard {
    @FXML
    private VBox vbox;
	@FXML
    private ImageView bookImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label locationOrDateLabel;

    public void setExtraDetailsVisible(boolean visible) {
        locationOrDateLabel.setVisible(visible);
    }

    public void setBookData(Book book) {
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthors());
        genreLabel.setText(book.getGenre());

        if (book.getLocationOrDate() != null) {
            locationOrDateLabel.setText(book.getLocationOrDate());
        } else {
            locationOrDateLabel.setText("Shelf " + book.getLocation());
        }

        // Set the book's image
        String url = getClass().getResource("/resources/book_covers/" + book.getImage()).toExternalForm();
        Image image = new Image(url);
		bookImageView.setImage(image);
	    if (image.isError()) {
	         System.out.println("Error loading image: " + image.getException().getMessage());
	    }

        Tooltip tooltip = new Tooltip(book.getDescription());
        tooltip.setWrapText(true);
        tooltip.setWidth(200);
        Tooltip.install(vbox, tooltip);
    }
}