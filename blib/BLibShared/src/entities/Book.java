package entities;

import java.io.Serializable;

public class Book implements Serializable {
	private int id;
	private String title;
	private String authors;
	private String genre;
	private String description;
	private String image;
  	private String location;
	private String locationOrDate;

	private boolean canOrder = false;

	public Book(int id, String title, String authors, String genre, String description, String image, String location) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.genre = genre;
		this.description = description;
		this.image = image;
		this.location = location;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
  
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getDescription() {
		return description;
	}

	public String getLocationOrDate() {
		return locationOrDate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocationOrDate(String locationOrDate) {
		this.locationOrDate = locationOrDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return title;
	}

    public boolean isCanOrder() {
        return canOrder;
    }

    public void setCanOrder(boolean canOrder) {
        this.canOrder = canOrder;
    }
}