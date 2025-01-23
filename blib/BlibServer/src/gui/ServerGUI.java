package gui;

import java.net.InetAddress;
import java.net.URL;
import java.util.*;

import controllers.CommunicationManager;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import ocsf.server.ConnectionToClient;
/*
 * This class represents the GUI for managing server connections.
 * It listens for incoming client connections and displays their status in a table.
 */
public class ServerGUI extends AbstractScreen implements Initializable {
	// Inner class representing a connection to be shown in the table
	public class TableConnection {
		public InetAddress address;
		public boolean connected = false;
		// Constructor to initialize the connection with a specific address
		public TableConnection(InetAddress address) {
			this.address = address;
		}
		// Returns the status of the connection ("Connected" or "Disconnected")
		public String getStatus() {
			return connected ? "Connected" : "Disconnected";
		}
		// Override equals to compare connections based on IP address and hostname
		
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
	 * Updates the connections table with new connections from the server.
	 * 
	 * This method is called to refresh the connection status by comparing the active connections
	 * with the ones already shown in the table.
	 * 
	 * @param connections Array of threads representing the active connections
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
		// Update the connection status for each displayed connection
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

	/**
	 * Initializes the GUI, setting up the table and columns.
	 * This method is called when the FXML file is loaded.
	 */
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
		connectionTable.getColumns().addAll(ipColumn, hostColumn, statusColumn);
	}

    /**
     * This function opens a new window with the server settings
     */
    public void openSettings() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Server settings");
        dialog.setHeaderText("Communication settings");

		// Set the button types.
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

		// Create the emailServer and smsServer labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField emailServer = new TextField();
        emailServer.setText(CommunicationManager.emailServer);
        emailServer.setMinWidth(300);
        TextField smsServer = new TextField();
        smsServer.setText(CommunicationManager.smsServer);

        grid.add(new Label("Email Server:"), 0, 0);
        grid.add(emailServer, 1, 0);
        grid.add(new Label("SMS Server:"), 0, 1);
        grid.add(smsServer, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a emailServer-smsServer-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(emailServer.getText(), smsServer.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(emailSms -> {
            CommunicationManager.emailServer = emailSms.getKey();
            CommunicationManager.smsServer = emailSms.getValue();
        });
    }
}