package base;

import controllers.Auth;
import entities.Message;
import ocsf.client.AbstractClient;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles communication between server and client on the client side.
 */
public class LibraryClient extends AbstractClient
{
	public static List<Message> awaitingMessages;

	/**
	 * Constructs an instance of the LibraryClient.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 */
	public LibraryClient(String host, int port) throws IOException {
		super(host, port); //Call the superclass constructor
		awaitingMessages = new ArrayList<>();
		openConnection();
	}

	//Instance methods ************************************************
	
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@Override
	public void handleMessageFromServer(Object msg) {
		Message msgFromServer = (Message)msg;		
		System.out.println("--> handleMessageFromServer: " + msgFromServer);

		int id = msgFromServer.getId();
		for(Message awaitingMsg : awaitingMessages) {
			if (awaitingMsg.getId() == id) {
				if (msgFromServer.isFatalError()) {
					awaitingMsg.setError(true);
					awaitingMsg.setAwaiting(false);
					awaitingMsg.setObject("Fatal error");
				} else {
					awaitingMsg.setObject(msgFromServer.getObject());
					awaitingMsg.setError(msgFromServer.isError());
					awaitingMsg.setAwaiting(false);
				}
			}
		}
	}

	/**
	 * This method handles all data coming from the UI   
	 * and Sends a message to the server & waits for a response.         
	 *
	 * @param msgToServer The message from the UI.
	 */
	public Message sendMessageToServer(Message msgToServer) {
		try {
			msgToServer.setAwaiting(true); // Mark the message as awaiting response
			msgToServer.setUser(Auth.getInstance().getUser()); // Set the user that sent the message
			awaitingMessages.add(msgToServer);// Add the message to the awaiting list
			sendToServer(msgToServer);// Send the message to the server

			int timePassed = 0;
			/*
			 * Checks if a message sent to the server has been
			 * processed and a response has been received.
			 */
			while (msgToServer.isAwaiting()) {
				try {
					Thread.sleep(100);
					timePassed += 100;
				} catch (InterruptedException e) {
					System.out.println("Interrupted..");
				}

				if (timePassed > 10 * 1000) {
					System.out.println(String.format("Warning: request %d has timed out after 30 seconds...", msgToServer.getId()));
					awaitingMessages.remove(msgToServer);
					return msgToServer.errorReply("Timed out");
				}
			}

			return msgToServer;// Return the server's response
		}
		catch(IOException e) {// Terminate the application in case of an error
			e.printStackTrace();
			System.exit(1);
			return null; // This will never happen
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		}
		catch(IOException e) {}
	}
}
