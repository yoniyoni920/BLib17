package gui;

import java.net.URISyntaxException;

import entities.Book;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookCard {
	@FXML
    private ImageView bookImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;

    public void setBookData(Book book) {
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthors());

        // Set the book's image
        Image image;
		image = new Image(book.getImage());
		System.out.println(book.getImage());
		bookImageView.setImage(image);
	    if (image.isError()) {
	         System.out.println("Error loading image: " + image.getException().getMessage());
	    }
    }
}