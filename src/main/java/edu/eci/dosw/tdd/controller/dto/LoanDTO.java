package edu.eci.dosw.tdd.controller.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class LoanDTO {
	private String id;
	private String userId;
	private String bookId;
	private LocalDate loanDate;
	private LocalDate returnDate;
	private String status;
}
