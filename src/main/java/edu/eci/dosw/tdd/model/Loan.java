package edu.eci.dosw.tdd.model;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {

	public enum Status {
		ACTIVE,
		RETURNED
	}

	private Book book;
	private User user;
	private LocalDate loanDate;
	private Status status;
	private LocalDate returnDate;

	public Loan() {
	}

	public Loan(Book book, User user, LocalDate loanDate, Status status, LocalDate returnDate) {
		this.book = book;
		this.user = user;
		this.loanDate = loanDate;
		this.status = status;
		this.returnDate = returnDate;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Loan loan = (Loan) o;
		return Objects.equals(book, loan.book) && Objects.equals(user, loan.user) && Objects.equals(loanDate, loan.loanDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(book, user, loanDate);
	}
}