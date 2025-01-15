
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
 * This class controls the client side
 *
 * @deprecated Will be removed soon due to being completely pointless
 */
public class ClientController {
    public Message sendToServer(Message msg) {
        return ClientApplication.getInstance().sendMessageToServer(msg);
    }

    public void sendToServer(Message msg, Consumer<Message> then) {
        Thread t = new Thread(() -> {
            Message result = sendToServer(msg);
            Platform.runLater(() -> then.accept(result));
        });
        t.start();
    }
}