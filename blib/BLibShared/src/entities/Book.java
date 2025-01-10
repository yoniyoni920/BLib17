package entities;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {

	private int bookId;
	private String title;
	private String authors;
	private String genre;
	private String description;
	private String image;
	private String location;
	private String locationOrDate;
	//private List<BookCopy> copies;
	public Book(int bookId, String title, String authors, String genre, String description, String image, String location, String locationOrDate) {
		this.bookId = bookId;
		this.title = title;
		this.authors = authors;
		this.genre = genre;
		this.description = description;
		this.image = image;
		this.location = location;
		this.locationOrDate = locationOrDate;
	}

	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
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
}