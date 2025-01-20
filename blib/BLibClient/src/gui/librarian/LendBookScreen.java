package gui.librarian;

import base.Action;
import base.ClientApplication;
import controllers.BookScanner;
import entities.Book;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import gui.AbstractScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.time.LocalDate;


public class LendBookScreen extends AbstractScreen {
    @FXML
    TextField bookIdTextField;
    @FXML
    Label bookIdAlert;
    @FXML
    Label userAlert;
    @FXML
    DatePicker returnDatePicker;
    @FXML
    DatePicker lendDatePicker;
    @FXML
    TextField subID;

    @FXML
    public void initialize() {
        lendDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            returnDatePicker.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setDisable(item.isAfter(lendDatePicker.getValue().plusWeeks(2)) || item.isBefore(lendDatePicker.getValue()));
                }
            });
            returnDatePicker.setValue(lendDatePicker.getValue().plusWeeks(2));
        });
        lendDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isBefore(LocalDate.now()));
            }
        });

        lendDatePicker.setValue(LocalDate.now());

        bookIdTextField.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1 && aBoolean) bookTextFieldChanged();
        });
        subID.focusedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (!t1 && aBoolean) userTextFieldChanged();
        });
    }
    // Validate the entered book ID and fetch book details from the server
    public void bookTextFieldChanged() {
        ClientApplication.chat.sendToServer(new Message(Action.GET_BOOK_BY_ID, bookIdTextField.getText()), message -> {
            if (message.isError()) {
                bookIdAlert.setText(message.getObject().toString());
                bookIdAlert.setVisible(true);
            } else {
                Book book = (Book) message.getObject();
                bookIdAlert.setText(book.getTitle());
                bookIdAlert.setVisible(true);
            }
        });
    }
    // Validate the entered subscriber ID and fetch subscriber details from the server
    public void userTextFieldChanged() {
        ClientApplication.chat.sendToServer(new Message(Action.GET_SUBSCRIBER_BY_ID, subID.getText()), message -> {
            if (message.isError()) {
                userAlert.setText(message.getObject().toString());
                userAlert.setTextFill(Color.RED);
                userAlert.setVisible(true);
            } else {
                Subscriber user = (Subscriber) message.getObject();
                userAlert.setText(user.getName() + " " + user.getLastName());
                userAlert.setTextFill(Color.DODGERBLUE);
                userAlert.setVisible(true);
            }
        });
    }

    public void searchBooskByName() {
        // TODO - implement LendBookScreen.searchBooskByName
        throw new UnsupportedOperationException();
    }
    // Activates the book scanner to fetch the book ID
    public void activateScanner() {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    String bookId = BookScanner.getInstance().Scan();
                    bookIdTextField.setText(bookId);
                    bookTextFieldChanged();

                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        });
        thread.start();
    }
    // Submit the lending process
    public void submitLend() {
        Integer sub = null, bookId = null;
        try {
            sub = Integer.parseInt(subID.getText());
        } catch (NumberFormatException e) {
            userAlert.setText("Subscriber ID is not valid!");
            userAlert.setVisible(true);
            return;
        }
        try {
            bookId = Integer.parseInt(bookIdTextField.getText());
        } catch (NumberFormatException e) {
            bookIdAlert.setText("Book ID is not valid!");
            bookIdAlert.setVisible(true);
            return;
        }
        Message msg = new Message(Action.LEND_BOOK, new BookCopy(0, bookId, lendDatePicker.getValue(), returnDatePicker.getValue(), sub, sub));
        ClientApplication.chat.sendToServer(msg, message -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (message.isError()) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Lending Error");
                alert.setContentText(message.getObject().toString());
                alert.showAndWait();
            } else {
                BookCopy bookCopy = (BookCopy) message.getObject();
                if (bookCopy.getOrderSubscriberId() == -1) {
                    alert.setHeaderText("Can't lend book");
                    alert.setContentText(bookIdAlert.getText() + " has no available copies for ordering or borrowing!");
                    alert.showAndWait();
                } else if (bookCopy.getBorrowSubscriberId() == -1) {
                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Can't lend book");
                    alert.setContentText(bookIdAlert.getText() + " isn't available until " + bookCopy.getReturnDate() + " would you like to order it?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(buttonType -> {
                        if(buttonType == ButtonType.NO) return;
                        Alert orderAlert = new Alert(Alert.AlertType.INFORMATION);
                        orderAlert.setHeaderText("Ordering book");
                        orderAlert.setContentText("Trying to make an order for " + bookIdAlert.getText());
                        ClientApplication.chat.sendToServer(new Message(Action.ORDER_BOOK, bookCopy), message1 -> {
                            Alert orderResultAlert = new Alert(Alert.AlertType.INFORMATION);
                            if(message1.isError()) {
                                orderResultAlert.setAlertType(Alert.AlertType.ERROR);
                                orderResultAlert.setHeaderText("Ordering Error");
                                orderResultAlert.setContentText(message1.getObject().toString());
                            }else {
                                orderResultAlert.setAlertType(Alert.AlertType.INFORMATION);
                                orderResultAlert.setHeaderText("Book Ordered successfully");
                                BookCopy copy = (BookCopy) message1.getObject();
                                orderResultAlert.setContentText(bookIdAlert.getText() + " has been ordered for subscriber " + userAlert.getText() + " and will be available on " + copy.getReturnDate());
                            }
                            orderResultAlert.show();
                            orderAlert.close();
                        });
                        orderAlert.showAndWait();
                    });
                }else {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Lent Successfully");
                    alert.setContentText(bookIdAlert.getText() + " was lent successfully to " + userAlert.getText() + " with a return date of " + bookCopy.getReturnDate());
                    alert.showAndWait();
                }
            }
        });
    }

}