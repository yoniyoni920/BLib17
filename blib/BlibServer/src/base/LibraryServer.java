package base;

import java.io.*;

import controllers.LoginControl;
import controllers.SubscriberControl;
import entities.Message;
import entities.User;
import ocsf.server.*;

public class LibraryServer extends AbstractServer 
{  
  /**
   * Constructs an instance of the LibraryServer. 
   * handles messages between: client - server
   *                           server - DB.
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
			Message msgFromServer = ClientMessageHandler.handleMessage(msgFromClient, client);

			// Once done, we can send a reply back to the client
			if (msgFromServer != null) {
				msgFromServer.setId(msgFromClient.getId());
				client.sendToClient(msgFromServer);
			}
		} catch (Exception e) {
			System.err.println("Failed to handle message: " + msgFromClient);
			System.err.println(e);
			try { // One last attempt, just to tell the client there was a fatal error
				if (msgFromClient != null) {
					client.sendToClient(msgFromClient.fatalError());
				}
			} catch (Exception ex) {}
		}
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
