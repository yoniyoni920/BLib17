package base;

import controllers.LoginControl;
import controllers.RegisterUser;
import controllers.SubscriberControl;
import entities.Message;
import entities.User;
import ocsf.server.ConnectionToClient;

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
        actions.put(Action.REGISTER, ClientMessageHandler::RegisterSubscriber);
        
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
    
    
    
    public static Message RegisterSubscriber(Message msg, ConnectionToClient client) {
    	String[] args = (String[])msg.getObject();
    	msg = RegisterUser.registerAction(args[0], args[1],args[2],args[3],args[4]);
        return msg;
    }
    
    
    
    
    
    
    
    
    
    
}


