package entities;

public class Subscriber extends User {
	private String status;
	public Subscriber() {
		super();
	}
	public Subscriber(String id, String firstName, String lastName) {
		super(id,firstName,lastName);
		this.status = "Not Frozen";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
