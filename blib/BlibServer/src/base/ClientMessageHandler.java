package base;

import controllers.BookControl;
import controllers.LoginControl;
import controllers.SubscriberControl;
import entities.Message;
import entities.User;
import ocsf.server.ConnectionToClient;

import java.time.LocalDate;
import java.util.HashMap;
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

        return msgFromClient.errorReply("Found no such action! " + msgFromClient.getAction());
    }

    /* ------- Actions -------*/

    /**
     * Method defining each action and reference to the method it will call
     */
    private void setupActions() {
        actions.put(Action.LOGIN, ClientMessageHandler::login);
        actions.put(Action.UPDATE_SUBSCRIBER, ClientMessageHandler::updateSubscriber);
        actions.put(Action.SEARCH_BOOKS,ClientMessageHandler::searchBooks);
        actions.put(Action.GET_BORROW_TIMES_REPORT, ClientMessageHandler::getBorrowTimesReport);
        actions.put(Action.GET_SUBSCRIBER_STATUS_REPORT, ClientMessageHandler::getSubscriberStatusReport);
        actions.put(Action.GER_REPORT_DATES, ClientMessageHandler::getReportDates);
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
        SubscriberControl.updateInfo((String[])msg.getObject());
        return msg.reply("Success");
    }

    public static Message searchBooks(Message msg, ConnectionToClient client) {
        String[] search = (String[])msg.getObject();
        return msg.reply(BookControl.searchBooks(search[0], search[1]));
    }

    public static Message getBorrowTimesReport(Message msg, ConnectionToClient client) {
        Object[] params = (Object[])msg.getObject();
        return msg.reply(BookControl.getBorrowTimesReport((LocalDate)params[0], (Integer)params[1]));
    }

    public static Message getSubscriberStatusReport(Message msg, ConnectionToClient client) {
        return msg.reply(SubscriberControl.getSubscriberStatusReport((LocalDate)msg.getObject()));
    }

    public static Message getReportDates(Message msg, ConnectionToClient client) {
        return msg.reply(SubscriberControl.getReportDates());
    }
}
