package edu.eci.dosw.tdd.model;

import java.util.Objects;

public class Book {

	private String title;
	private String author;
	private String id;
	private boolean available;

	public Book() {
	}

	public Book(String title, String author, String id) {
		this(title, author, id, true);
	}

	public Book(String title, String author, String id, boolean available) {
		this.title = title;
		this.author = author;
		this.id = id;
		this.available = available;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Book book = (Book) o;
		return Objects.equals(id, book.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}