package client;

import ocsf.client.*;
import entities.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryClient extends AbstractClient
{
	
	/**
	 * This Class is the message handles between server and Client
	 */
	ClientController clientUI; 
	public static List<Message> awaitingMessages;    

	
	/**
	 * Constructs an instance of the LibraryClient.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	public LibraryClient(String host, int port, ClientController clientUI) throws IOException {
		super(host, port); //Call the superclass constructor
		this.clientUI = clientUI;
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
				awaitingMsg.setObject(msgFromServer.getObject());
				awaitingMsg.setError(msgFromServer.isError());
				awaitingMsg.setAwaiting(false);
			}
		}
	}

	/**
	 * This method handles all data coming from the UI            
	 *
	 * @param message The message from the UI.    
	 */
	public Message sendMessageToServer(Message msgToServer) {
		try {
			msgToServer.setAwaiting(true);
			awaitingMessages.add(msgToServer);
			sendToServer(msgToServer);

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

				if (timePassed > 5 * 1000) {
					System.out.println(String.format("Warning: request %d has timed out after 60 seconds...", msgToServer.getId()));
					awaitingMessages.remove(msgToServer);
					return null; //TODO: implement timeout exception
				}
			}

			return msgToServer;
		}
		catch(IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server: Terminating client."+ e);
			quit();
		}

		return null;
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
