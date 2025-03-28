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
     * @param message The message to send
     * @return Response message from server
     */
    public static Message sendMessage(Message message) {
        return ClientApplication.getInstance().sendMessageToServer(message);
    }

    /**
     * Sends a message without an object and makes a Message object for you
     * @param action The action of the message
     * @return Response message from server
     */
    public static Message sendMessage(Action action) {
        return sendMessage(new Message(action));
    }

    /**
     * Sends a message and makes the Message object for you
     * @param action The action of the message
     * @param object The object to send in the message
     * @return Response message from server
     */
    public static Message sendMessage(Action action, Object object) {
        return sendMessage(new Message(action, object));
    }

    /**
     * Sends a message in either different thread or in a synchronized way.
     * Useful in cases you want to run it in async but then do want.
     * <br>
     * A good example of this being utilized is in the search books pages, we want the user to open
     * the screen and instantly see the books instead of seeing "no books found".
     * @param msg The message to send
     * @param then The function to run on response
     * @param async Whether to run this asynchronized or not
     */
    public static void sendMessage(Message msg, Consumer<Message> then, boolean async) {
        if (async) {
            sendMessage(msg, then);
        } else {
            then.accept(sendMessage(msg));
        }
    }

    /**
     * Sends a message in a different thread, this is useful if you call this is many times and don't wish to slowdown
     * the UI thread.
     * @param msg The message to send
     * @param then The function to run on response
     */
    public static void sendMessage(Message msg, Consumer<Message> then) {
        Thread t = new Thread(() -> {
            Message result = sendMessage(msg);
            Platform.runLater(() -> then.accept(result));
        });
        t.start();
    }
}
