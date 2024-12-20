package server;

import java.io.*;

import controllers.LoginControl;
import controllers.SubscriberControl;
import entities.Message;
import entities.User;
import ocsf.server.*;

public class LibraryServer extends AbstractServer 
{
  //Class variables *************************************************
  
  
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   * 
   */
	public LibraryServer(int port) 
	{
		super(port);
	}
  
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	@Override
	public void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
		Message msgFromClient = (Message)msg;
		
		System.out.println("Message received: " + msgFromClient);

		try {
			// Handle the action. Usually coming with a response back
			Message msgFromServer = handleAction(msgFromClient, client);

			// Once done, we can send a reply back to the client
			if (msgFromServer != null) {
				msgFromServer.setId(msgFromClient.getId());
				client.sendToClient(msgFromServer);
			}
		} catch (Exception e) {
			System.err.println("Could not send to client");
			System.err.println(e);
		}
	}
	
	public Message handleAction(Message msgFromClient, ConnectionToClient client) {
		String actionName = msgFromClient.getAction();
		// Find and handle any action from client
		if (actionName.equals("login")) {
			return login(msgFromClient, client);
		}
		else if(actionName.equals("update")) {
			return update(msgFromClient, client);
		}
		else {
			return msgFromClient.errorReply("Found no such action! " + msgFromClient.getAction());
		}
	}

    public Message login(Message msg, ConnectionToClient client) {
    	User user = LoginControl.loginAction((String)msg.getObject());
        if (user != null) {
            return msg.reply(user);
        } else {
            return msg.errorReply("Couldn't login user!");
        }
    }
    public Message update(Message msg, ConnectionToClient client) {
    	SubscriberControl.updateInfo((String[])msg.getObject());
    	return msg.reply("Success");
    }

	/**
	 * Called when the server starts listening for connections.
	 */
    @Override
	protected void serverStarted()
	{
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * Called when the server stops listening for connections.
	 */
    @Override
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}  
}
