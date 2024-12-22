package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.collections.ObservableList;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ocsf.server.ConnectionToClient;
/*
 * 
 */
public class ServerGUI extends AbstractScreen implements Initializable {
	public class TableConnection {
		public InetAddress address;
		public boolean connected = false;
		
		public TableConnection(InetAddress address) {
			this.address = address;
		}

		public String getStatus() {
			return connected ? "Connected" : "Disconnected";
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TableConnection other = (TableConnection) obj;
			return Objects.equals(address.getHostAddress(), other.address.getHostAddress())
					&& Objects.equals(address.getHostName(), other.address.getHostName());
		}
	}

	@FXML
	private Button btnExit = null;

	@FXML
	private TableView<TableConnection> connectionTable;
	
	private ObservableList<TableConnection> shownConnections;
	
	/**
	 * This receives connections from the ServerApplication and updates the connections list
	 * @param connections
	 */
	public void updateConnections(Thread[] connections) {		
		for (Thread connectionThread : connections) {
			if (connectionThread instanceof ConnectionToClient) {
				InetAddress address = ((ConnectionToClient)connectionThread).getInetAddress();
				TableConnection tableConnection = new TableConnection(address);
				if (!shownConnections.contains(tableConnection)) {
					shownConnections.add(tableConnection);
				}
			}
		}
		
		for (TableConnection tableConnection : shownConnections) {
			tableConnection.connected = false;
			for (Thread connectionThread : connections) {
				if (connectionThread instanceof ConnectionToClient) {
					InetAddress conAddress = ((ConnectionToClient)connectionThread).getInetAddress();
					TableConnection addressTableConnection = new TableConnection(conAddress);
					
					if (tableConnection.equals(addressTableConnection)) {
						tableConnection.connected = true;
						break;
					}
				}
			}
		}
		
		connectionTable.refresh();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		shownConnections = FXCollections.observableArrayList();
		connectionTable.setItems(shownConnections);

		// Create 2 relevant columns
		TableColumn<TableConnection, String> ipColumn = new TableColumn<>("IP");
		TableColumn<TableConnection, String> hostColumn = new TableColumn<>("Host");
		TableColumn<TableConnection, String> statusColumn = new TableColumn<>("Status");
		// This maps the cells into the String[] value we set above
		ipColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().address.getHostAddress()));
		hostColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().address.getHostName()));
		statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
		connectionTable.getColumns().add(ipColumn);
		connectionTable.getColumns().add(hostColumn);
		connectionTable.getColumns().add(statusColumn);
	}
}