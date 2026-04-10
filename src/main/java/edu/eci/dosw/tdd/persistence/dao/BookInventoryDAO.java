package edu.eci.dosw.tdd.persistence.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_inventory")
public class BookInventoryDAO {

	@Id
	@Column(name = "book_id", nullable = false, length = 50)
	private String bookId;

	@OneToOne(optional = false)
	@MapsId
	@JoinColumn(name = "book_id")
	private BookDAO book;

	@Column(name = "copies", nullable = false)
	private Integer copies;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public BookDAO getBook() {
		return book;
	}

	public void setBook(BookDAO book) {
		this.book = book;
	}

	public Integer getCopies() {
		return copies;
	}

	public void setCopies(Integer copies) {
		this.copies = copies;
	}
}
