package edu.eci.dosw.tdd.core.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Loan {
	private String id;
	private Book book;
	private User user;
	private LocalDate loanDate;
	private LocalDate returnDate;
	private Status status;
}
