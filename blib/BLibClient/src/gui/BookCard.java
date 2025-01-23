package gui;

import java.net.URISyntaxException;

import base.Action;
import controllers.Auth;
import entities.Book;
import entities.BookOrder;
import entities.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import services.ClientUtils;
import services.InterfaceUtils;

/**
 * The BookCard class is a controller for the book card UI component.
 * It handles setting book data and managing the visibility of extra details.
 */
public class BookCard {
    @FXML public Button orderButton;
    Book book;

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
    /**
     * Sets the visibility of the extra details (location or date) label.
     * 
     * @param visible true to make the extra details visible, false otherwise
     */
    public void setExtraDetailsVisible(boolean visible) {
        locationOrDateLabel.setVisible(visible);
    }
    /**
     * Sets the book data to be displayed in the card.
     * 
     * @param book the Book object containing the data to display
     */
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
        tooltip.setMaxWidth(500);
        Tooltip.install(vbox, tooltip);

        orderButton.setVisible(Auth.getInstance().getSubscriber() != null && book.isCanOrder());

        this.book = book;
    }

    public void orderBook(ActionEvent event) {
        BookOrder order = new BookOrder(
            Auth.getInstance().getSubscriber().getSubscriberId(),
            book.getId()
        );
        ClientUtils.sendMessage(new Message(Action.ORDER_BOOK, order), message1 -> {
            Alert orderResultAlert = new Alert(Alert.AlertType.INFORMATION);
            if(message1.isError()) {
                orderResultAlert.setAlertType(Alert.AlertType.ERROR);
                orderResultAlert.setHeaderText("Ordering Error");
                orderResultAlert.setContentText(message1.getObject().toString());
            }
            else {
                BookOrder bookOrder1 = (BookOrder)message1.getObject();
                if(bookOrder1.getOrderId() == -1){
                    orderResultAlert.setAlertType(Alert.AlertType.INFORMATION);
                    orderResultAlert.setHeaderText("Can't Order Book");
                    orderResultAlert.setContentText("Book can't be ordered at the moment!");
                }else {
                    orderResultAlert.setAlertType(Alert.AlertType.INFORMATION);
                    orderResultAlert.setHeaderText("Book Ordered Successfully");
                    orderResultAlert.setContentText(String.format("%s has been ordered successfully and is expected to be available by %s",
                             book.getTitle(), InterfaceUtils.formatDate(bookOrder1.getOrderDate())));
                }
                orderButton.setVisible(false);
            }
            orderResultAlert.show();
        });
    }
}