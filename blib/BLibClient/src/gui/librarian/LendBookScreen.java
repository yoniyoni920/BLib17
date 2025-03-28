package gui.librarian;

import base.Action;
import controllers.BookScanner;
import entities.*;
import gui.AbstractScreen;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import services.ClientUtils;
import services.InterfaceUtils;

import java.time.LocalDate;
import java.time.LocalTime;

public class LendBookScreen extends AbstractScreen {
    @FXML
    private TextField bookIdTextField;
    @FXML
    private Label bookIdAlert;
    @FXML
    private Label userAlert;
    @FXML
    private DatePicker returnDatePicker;
    @FXML
    private DatePicker lendDatePicker;
    @FXML
    private TextField subID;

    private Subscriber subscriber;

    @Override
    public void openScreen(Object... args) {
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
            setDisable(item.isBefore(LocalDate.now().minusWeeks(2)));
            }
        });

        lendDatePicker.setValue(LocalDate.now());
    }

    /**
     * This function validate the entered book ID and fetch book details from the server
     * @param keyEvent
     */
    public void bookTextFieldChanged(KeyEvent keyEvent) {
        ClientUtils.sendMessage(new Message(Action.GET_BOOK_BY_ID, bookIdTextField.getText()), message -> {
            if (message.isError()) {
                bookIdAlert.setText(message.getObject().toString());
                bookIdAlert.setTextFill(Color.RED);
                bookIdAlert.setVisible(true);
            } else {
                Book book = (Book) message.getObject();
                bookIdAlert.setText(book.getTitle());
                bookIdAlert.setTextFill(Color.DODGERBLUE);
                bookIdAlert.setVisible(true);
            }
        });
    }

    /**
     * This function validate the entered subscriber ID and fetch subscriber details from the server
     * @param keyEvent
     */
    public void userTextFieldChanged(KeyEvent keyEvent) {
        ClientUtils.sendMessage(new Message(Action.GET_SUBSCRIBER_BY_ID, subID.getText()), message -> {
            if (message.isError()) {
                userAlert.setText(message.getObject().toString());
                userAlert.setTextFill(Color.RED);
                userAlert.setVisible(true);
            } else {
                subscriber = (Subscriber)message.getObject();
                userAlert.setText(subscriber.toString());
                userAlert.setTextFill(Color.DODGERBLUE);
                userAlert.setVisible(true);
            }
        });
    }

    /**
     * This function activates the book scanner to fetch the book ID
     */
    public void activateScanner() {
        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                try {
                    String bookId = BookScanner.getInstance().Scan();
                    bookIdTextField.setText(bookId);
                    bookTextFieldChanged(null);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            });
        });
        thread.start();
    }

    /**
     * This function submits the lending process
     */
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

        LocalTime now = LocalTime.now();
        BookCopy sendCopy = new BookCopy(0, bookId, lendDatePicker.getValue().atTime(now),
                returnDatePicker.getValue().atTime(now), subscriber.getSubscriberId());
        Message msg = new Message(Action.LEND_BOOK, sendCopy);
        ClientUtils.sendMessage(msg, message -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (message.isError()) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText("Lending Error");
                alert.setContentText(message.getObject().toString());
                alert.showAndWait();
            } else {
                if (message.getObject() == null) {
                    alert.setHeaderText("Can't lend book");
                    alert.setContentText(bookIdAlert.getText() + " has no available copies for ordering or borrowing!");
                    alert.showAndWait();
                } else if (message.getObject() instanceof BookOrder) {
                    BookOrder bookOrder = (BookOrder) message.getObject();
                    alert.setAlertType(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Can't lend book");
                    alert.setContentText(bookIdAlert.getText() + " isn't available until " + InterfaceUtils.formatDate(bookOrder.getOrderDate())
                            + " would you like to order it?");
                    alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(buttonType -> {
                        if(buttonType == ButtonType.NO) return;
                        Alert orderAlert = new Alert(Alert.AlertType.INFORMATION);
                        orderAlert.setHeaderText("Ordering book");
                        orderAlert.setContentText("Trying to make an order for " + bookIdAlert.getText());
                        ClientUtils.sendMessage(new Message(Action.ORDER_BOOK, bookOrder), message1 -> {
                            Alert orderResultAlert = new Alert(Alert.AlertType.INFORMATION);
                            if(message1.isError()) {
                                orderResultAlert.setAlertType(Alert.AlertType.ERROR);
                                orderResultAlert.setHeaderText("Ordering Error");
                                orderResultAlert.setContentText(message1.getObject().toString());
                            }
                            else {
                                BookOrder bookOrder1 = (BookOrder) message1.getObject();
                                if(bookOrder1.getOrderId() == -1){
                                    orderResultAlert.setAlertType(Alert.AlertType.INFORMATION);
                                    orderResultAlert.setHeaderText("Can't order book");
                                    orderResultAlert.setContentText("Book can't be ordered at the moment!");
                                }else {
                                    orderResultAlert.setAlertType(Alert.AlertType.INFORMATION);
                                    orderResultAlert.setHeaderText("Book Ordered successfully");
                                    orderResultAlert.setContentText(bookIdAlert.getText() + " has been ordered for subscriber "
                                            + userAlert.getText() + " and will be available on " + InterfaceUtils.formatDate(bookOrder1.getOrderDate()));
                                }
                            }
                            orderResultAlert.show();
                            orderAlert.close();
                        });
                        orderAlert.showAndWait();
                    });
                }else if(message.getObject() instanceof BookCopy) {
                    BookCopy bookCopy = (BookCopy) message.getObject();
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Lent Successfully");
                    alert.setContentText(bookIdAlert.getText() + " was lent successfully to " + userAlert.getText() + " with a return date of " +
                            InterfaceUtils.formatDate(bookCopy.getReturnDate()));
                    alert.showAndWait();
                    closeScreen(null);
                }
            }
        });
    }

}