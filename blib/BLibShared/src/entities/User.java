package entities;

import java.io.Serializable;

/*
 * User class has three uses: 
 * 1. if needed to be used for unlogged users 
 * 2. to be sent when user want to loggin
 * 3. to be a parent for librarian and Subscriber 
 */

public class User implements Serializable{
	private String firstName;
	//private String lastName;	
	private String id;
	//private String password;	
	//private String role; // This will be used to differentiate between librarian and member. 
	
	public User() {
		
	}

	public User(String Id, String firstName) {
		this.id = Id;
		this.setFirstName(firstName);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
}
	
	