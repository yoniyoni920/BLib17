package entities;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
	private int id;
	private String title;
	private String authors;
	private String genre;
	private String description;
	private String image;
	private String location;

	public Book(int id, String title, String authors, String genre, String description, String image, String location) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.genre = genre;
		this.description = description;
		this.image = image;
		this.location = location;
	}

	public String getTitle() {
		return title;
	}

    public int getId() {
        return id;
    }
}