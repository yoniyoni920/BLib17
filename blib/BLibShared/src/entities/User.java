package entities;

import java.io.Serializable;

/*
 * User class has three uses: 
 * 1. if needed to be used for unlogged users 
 * 2. to be sent when user want to loggin
 * 3. to be a parent for librarian and Subscriber 
 */

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String lastName;
	private String id;
	private Role role;
	private String password;

	public User() {}

	public User(String id, String name, String lastName, String role, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = Role.valueOf(role.toUpperCase());
		this.password = password ;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setName(String name) {
		this.name = name ;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
}
	
	