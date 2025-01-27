package entities;

import java.io.Serializable;

/**
 * A class that handles basically all needs of a user (subscriber and librarian)
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private String lastName;
	private int id;
	private Role role;
	private String password;

	public User() {}

	public User(int id, String name, String lastName, String role, String password) {
		this.id = id;
		this.name = name;
		this.lastName = lastName;
		this.role = Role.valueOf(role.toUpperCase());
		this.password = password ;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
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
	
	