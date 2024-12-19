package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ocsf.server.ConnectionToClient;

public class ServerGUI implements Initializable {		
	@FXML
	private Button btnExit = null;

	@FXML
	private TableView<String[]> connectionTable;
	
	private ObservableList<String[]> shownConnections;
	
	/**
	 * This receives connections from the ServerApplication and updates the connections list
	 * @param connections
	 */
	public void updateConnections(Thread[] connections) {
		shownConnections.clear();

		for (Thread connectionThread : connections) {
			InetAddress address = ((ConnectionToClient)connectionThread).getInetAddress();
			shownConnections.add(new String[]{ address.getHostAddress(), address.getHostName() });
		}
		
		connectionTable.setItems(shownConnections);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println(hashCode());
		shownConnections = FXCollections.observableArrayList();
		System.out.println(shownConnections);
		connectionTable.setItems(shownConnections);

		// Create 2 relevant columns
		TableColumn<String[], String> column1 = new TableColumn<>("IP");
		TableColumn<String[], String> column2 = new TableColumn<>("Host");
		// This maps the cells into the String[] value we set above
		column1.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[0]));
		column2.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue()[1]));
		connectionTable.getColumns().add(column1);
		connectionTable.getColumns().add(column2);
	}
}