package base;

import controllers.BookCopyControl;
import controllers.BookControl;
import controllers.LoginControl;
import controllers.SubscriberControl;
import entities.BookCopy;
import entities.Message;
import entities.Subscriber;
import entities.User;
import ocsf.server.ConnectionToClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ClientMessageHandler {
    /* ------- Logic -------*/
    // This codes handles how actions work
    private Map<Action, BiFunction<Message, ConnectionToClient, Message>> actions;
    public static ClientMessageHandler instance;
    public static ClientMessageHandler getInstance() {
        if (instance == null) {
            instance = new ClientMessageHandler();
        }
        return instance;
    }

    // Define all actions here
    public ClientMessageHandler() {
        actions = new HashMap<>();
        setupActions();
    }

    /*
     * Reads a message from a client and decides what to do with it
     */
    public static Message handleMessage(Message msgFromClient, ConnectionToClient client) {
        Action actionName = msgFromClient.getAction();

        BiFunction<Message, ConnectionToClient, Message> actionFunc = getInstance().actions.get(actionName);
        if (actionFunc != null) {
            return actionFunc.apply(msgFromClient, client);
        }
        System.out.println(msgFromClient);
        return msgFromClient.errorReply("Found no such action! " + msgFromClient.getAction());
    }

    /* ------- Actions -------*/

    /**
     * Method defining each action and reference to the method it will call
     */
    private void setupActions() {
        actions.put(Action.LOGIN, ClientMessageHandler::login);
        actions.put(Action.UPDATE_SUBSCRIBER, ClientMessageHandler::updateSubscriber);
        actions.put(Action.RETRIEVE_BORROWEDBOOKS, ClientMessageHandler::retrieveBorrowedBooks);
        actions.put(Action.SEARCH_BOOKS, ClientMessageHandler::searchBooks);
    }

    /**
     * Handles logging subscribers or librarians
     */
    public static Message login(Message msg, ConnectionToClient client) {
        String[] args = (String[])msg.getObject();
        User user = LoginControl.loginAction(args[0], args[1]);
        if (user != null) {
            return msg.reply(user);
        }
        return msg.errorReply("Couldn't login user! ID may be wrong.");
    }

    /**
     * Handles updating info of a subscriber
     * @param msg
     * @param client
     * @return Message
     */
	  public static Message updateSubscriber(Message msg, ConnectionToClient client) {
        SubscriberControl.updateInfo((List<String>)msg.getObject());
        return msg.reply("Success");
    }
  
    //handles retrieving the borrowed books for a specific subscriber 
    public static Message retrieveBorrowedBooks(Message msg , ConnectionToClient client) {
        List<BookCopy> borrowedBooks = BookCopyControl.retrieveBorrowedBooks((Subscriber)msg.getObject());
        return msg.reply(borrowedBooks);
    }

    public static Message searchBooks(Message msg, ConnectionToClient client) {
        String[] searchInfo = (String[]) msg.getObject();
        return msg.reply(BookControl.searchBooks(searchInfo[0], searchInfo[1]));
    }
}
