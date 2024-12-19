package client.entities;

/*
 * User class has three uses: 
 * 1. if needed to be used for unlogged users 
 * 2. to be sent when user want to loggin
 * 3. to be a parent for librarian and Subscriber 
 */

public class User {
	private String firstName;
	private String lastName;	
	private String id;
	private String password;	
	private String role; // This will be used to differentiate between librarian and member. 
	
	public User() {
		
	}
	
	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}
	public User(String Id, String firstName, String lastName) {
		this.id = Id;
		this.setFirstName(firstName);
		this.setLastName(lastName);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
}
	
	