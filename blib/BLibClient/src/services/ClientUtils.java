package services;

import base.Action;
import base.ClientApplication;
import entities.Message;
import javafx.application.Platform;

import java.util.function.Consumer;

/**
 * This is a utility class that is used for simplifying communication
 * It saves you from getting an instance of the client application and also has some goodies
 */
public class ClientUtils {
    /**
     * Sends a message object to the server
     * @param message
     * @return Response message from server
     */
    public static Message sendMessage(Message message) {
        return ClientApplication.getInstance().sendMessageToServer(message);
    }

    /**
     * Sends a message without an object and makes a Message object for you
     * @param action
     * @return Response message from server
     */
    public static Message sendMessage(Action action) {
        return sendMessage(new Message(action));
    }

    /**
     * Sends a message and makes the Message object for you
     * @param action
     * @param object
     * @return Response message from server
     */
    public static Message sendMessage(Action action, Object object) {
        return sendMessage(new Message(action, object));
    }

    /**
     * Sends a message in a different thread, this is useful if you call this is many times and don't wish to slowdown
     * the UI thread.
     * @param msg
     * @param then
     */
    public static void sendMessage(Message msg, Consumer<Message> then) {
        Thread t = new Thread(() -> {
            Message result = sendMessage(msg);
            Platform.runLater(() -> then.accept(result));
        });
        t.start();
    }
}
