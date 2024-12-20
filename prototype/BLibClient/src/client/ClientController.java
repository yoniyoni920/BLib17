// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;
import java.io.*;
import client.*;
import entities.Message;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientController {
    //Class variables *************************************************

    //Instance variables **********************************************

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

    //Instance methods ************************************************
    public Message sendToServer(Message msg) {
        return client.sendMessageToServer(msg);
    }

    /**
     * This method overrides the method in the ChatIF interface.  It
     * displays a message onto the screen.
     *
     * @param message The string to be displayed.
     */
    public void display(String message) {
        System.out.println("> " + message);
    }
}
//End of ConsoleChat class