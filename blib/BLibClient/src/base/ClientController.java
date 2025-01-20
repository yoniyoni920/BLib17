
package base;
import java.io.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import base.*;
import entities.Message;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class controls the client-side operations, handling interactions with the server.
 * 
 * @deprecated Will be removed soon due to being considered redundant.
 */
public class ClientController {
    public Message sendToServer(Message msg) {
        return ClientApplication.getInstance().sendMessageToServer(msg);
    }

    
    
    

    /**
     * Sends a Message object to the server and processes the response using the provided Consumer.
     * This method runs the server communication on a separate thread to prevent blocking the UI.
     * 
     * @param msg  the Message object to send
     * @param then a Consumer to process the server's response on the JavaFX Application Thread
     */
    public void sendToServer(Message msg, Consumer<Message> then) {
        Thread t = new Thread(() -> {
        	// Send the message to the server and retrieve the response
            Message result = sendToServer(msg);
            // Ensure the Consumer is executed on the JavaFX Application Thread
            Platform.runLater(() -> then.accept(result));
        });
        t.start();// Start the thread
    }
}