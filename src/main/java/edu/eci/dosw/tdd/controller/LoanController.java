package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public LoanDTO createLoan(@RequestBody LoanDTO request) {
		return LoanMapper.toDto(loanService.createLoan(request.getBookId(), request.getUserId()));
	}

	@PatchMapping("/return")
	public LoanDTO returnLoan(@RequestBody LoanDTO request) {
		return LoanMapper.toDto(loanService.returnLoan(request.getBookId(), request.getUserId()));
	}

	@GetMapping
	public List<LoanDTO> getAllLoans() {
		return loanService.getAllLoans().stream()
			.map(LoanMapper::toDto)
			.toList();
	}
}