package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import base.LibraryServer;
import base.ServerApplication;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import ocsf.server.ConnectionToClient;

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