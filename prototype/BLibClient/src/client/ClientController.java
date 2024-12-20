
package client;
import java.io.*;
import client.*;
import entities.Message;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class controls the client side 
 * 
 */
public class ClientController {
    /**
     * The instance of the client that created this ConsoleChat.
     */
    private LibraryClient client;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the ClientConsole UI.
     *
     * @param host The host to connect to.
     * @param port The port to connect on.
     */
    
    public ClientController(String host, int port) {
        try {
            client = new LibraryClient(host, port, this);
        } catch (IOException e) {
            System.out.println("Error: Can't setup connection!" + " Terminating client.");
            
        	Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Could not start BLib client!");
			alert.setHeaderText(String.format("Couldn't find any server at: %s:%d", host, port));
			alert.setContentText(e.toString());
			alert.showAndWait();
			System.exit(1);
        }
    }

    public LibraryClient getClient() {
    	return client;
    }

    public Message sendToServer(Message msg) {
        return client.sendMessageToServer(msg);
    }

    /**
     * displays a message onto the screen.
     */
    public void display(String message) {
        System.out.println("> " + message);
    }
}