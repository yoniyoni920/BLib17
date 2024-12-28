package entities;

import java.util.List;

public class Book {

	private int bookId;
	private String title;
	private String[] authors;
	private String genre;
	private String description;
	private List<BookCopy> copies;
	private Boolean isOrdered;

}