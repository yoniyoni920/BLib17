package gui;

import controllers.BookScanner;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class LendBookScreen extends AbstractScreen {
	@FXML
	TextField bookIdTextField;


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