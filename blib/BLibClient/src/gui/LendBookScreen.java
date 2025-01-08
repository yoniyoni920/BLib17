package gui;

import controllers.BookScanner;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;


public class LendBookScreen extends AbstractScreen {
	@FXML
	TextField bookIdTextField;
	@FXML
	Label noBookCopyErrLabel;
	@FXML
	Label subFrozenErrLabel;
	@FXML
	DatePicker returnDatePicker;
	@FXML
	DatePicker lendDatePicker;

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