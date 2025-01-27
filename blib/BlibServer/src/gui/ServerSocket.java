package gui;

import base.ServerApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * controller for ServerSocket.fxml
 */
public class ServerSocket extends AbstractScreen{
	@FXML
	private TextField Porttxt = null;

	@FXML
	private Label portWarning;

	/**
	 * Creates Server with wanted port
	 * @param event
	 * @throws Exception
	 */
	public void Create(ActionEvent event) throws Exception {
		String port = Porttxt.getText();
		portWarning.setVisible(false);
		if (port.trim().isEmpty()) {
			portWarning.setVisible(true);
		} else {
			ServerApplication.getInstance().createServer(port);
		}
	}
}