package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.model.Loan;
import edu.eci.dosw.tdd.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LibraryService libraryService;

	public LoanController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Loan createLoan(@RequestBody LoanRequest request) {
		return libraryService.createLoan(request.getBookId(), request.getUserId());
	}

	@PatchMapping("/return")
	public Loan returnLoan(@RequestBody LoanRequest request) {
		return libraryService.returnLoan(request.getBookId(), request.getUserId());
	}

	@GetMapping
	public List<Loan> getAllLoans() {
		return libraryService.getAllLoans();
	}
}