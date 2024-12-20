package entities;

import java.io.Serializable;

public class Message implements Serializable{
	/*
	 * The idea Behind msg is that the first thing we need to know 
	 * is :what do we want to do? aka. why are we sending the msg 
	 * to the server?
	 * so there needs to be a String that will tell us that
	 * There should also be one so that the server will be able to send a general
	 * message back to the client if he needs to such as potential errors: "user doesnt exists"
	 * for example. lastly we want to send the object(data) we want to send with the wanted action
	 * for that purpose we will add:
	 * String action will say the 'action' we want the server to do
	 * String error this String will be used by the server to return Message to the client
	 * will be used to return error and will help with checks
	 * note that server will also use action when he returns message to the client
	 * but 'error' allows to code to be more easily understood
	 * and lastly we will need to add id for Serializable implementation:
	 * needed in order to allow messages to be sent correctly between server and client
	 * and the needed Entities for each corresponding action 
	 */
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -129263669966691571L;
	/* ------- Basic information -----*/
	private static int idCounter; // For counting IDs automatically
	private int id;
	private String action;
	/* ------- Entities Sent-----*/
	private Object object;
	
	// Whether this message is an error. object will be an object containing info about the error
	private boolean isError = false;
	
	// Handles awaiting the message
	private boolean isAwaiting = false;
	
	/* ------- Logic -------*/
	public Message() {
		id = idCounter++;
	}

	public Message(String action, Object object) {
		this();
		this.action = action;
		this.object = object;
	}

	/**
	 * Replies to the message by changing the object and isError value
	 * @param object
	 * @param isError
	 * @return Message
	 */
	public Message reply(Object object) {
		this.object = object;
		this.setError(false);
		return this;
	}
	
	public Message errorReply(Object object) {
		this.object = object;
		this.setError(true);
		return this;
	}

	/* ------- Getters & Setters -----*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public boolean isAwaiting() {
		return isAwaiting;
	}

	public void setAwaiting(boolean isAwaiting) {
		this.isAwaiting = isAwaiting;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", action=" + action + ", object=" + object + ", isError=" + isError
				+ ", isAwaiting=" + isAwaiting + "]";
	}
}
