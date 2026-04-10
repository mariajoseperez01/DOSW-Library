package edu.eci.dosw.tdd.persistence.nonrelational.document;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import edu.eci.dosw.tdd.core.model.Loan;

@Document("loans")
public class LoanDocument {

	@Id
	private String id;
	private String bookId;
	private String userId;
	private LocalDate loanDate;
	private Loan.Status status;
	private LocalDate returnDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDate getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDate loanDate) {
		this.loanDate = loanDate;
	}

	public Loan.Status getStatus() {
		return status;
	}

	public void setStatus(Loan.Status status) {
		this.status = status;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
}