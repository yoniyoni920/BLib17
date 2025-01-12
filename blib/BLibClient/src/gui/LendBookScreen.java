package gui;

import base.Action;
import base.ClientApplication;
import controllers.BookScanner;
import entities.Book;
import entities.Message;
import entities.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

		bookIdTextField.focusedProperty().addListener((observableValue, aBoolean, t1) ->{
			if(!t1) bookTextFieldChanged();
		});
		subID.focusedProperty().addListener((observableValue, aBoolean, t1) ->{
			if(!t1)userTextFieldChanged();
		});
	}

	public void bookTextFieldChanged(){
		Message	reply = ClientApplication.chat.sendToServer(new Message(Action.GET_BOOK_BY_ID, bookIdTextField.getText()));
		if(reply.isError()){
			bookIdAlert.setText(reply.getObject().toString());
			bookIdAlert.setVisible(true);
		}else{
			Book book = (Book) reply.getObject();
			bookIdAlert.setText(book.getTitle());
			bookIdAlert.setVisible(true);
		}
	}

	public void userTextFieldChanged(){
		Message reply = ClientApplication.chat.sendToServer(new Message(Action.GET_SUBSCRIBER_BY_ID, subID.getText()));
		if(reply.isError()){
			userAlert.setText(reply.getObject().toString());
			userAlert.setVisible(true);
		}else{
			Subscriber user = (Subscriber) reply.getObject();
			userAlert.setText(user.getFirstName() + " " + user.getLastName());
			userAlert.setVisible(true);
		}
	}

	public void searchBooskByName() {
		// TODO - implement LendBookScreen.searchBooskByName
		throw new UnsupportedOperationException();
	}

	public void activateScanner() {
		Thread thread = new Thread(() -> {
			try{
				String bookId = BookScanner.getInstance().Scan();
				bookIdTextField.setText(bookId);
				bookTextFieldChanged();

			}catch (InterruptedException ex){
				System.out.println(ex.getMessage());
			}
		});
		thread.start();
	}

	public void submitLend() {
		// TODO - implement LendBookScreen.submitLend
		throw new UnsupportedOperationException();
	}

}