package services;

import base.Action;
import base.ClientApplication;
import entities.Message;

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
}
