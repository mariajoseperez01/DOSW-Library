package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;

@RestController
@RequestMapping("/loans")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@PostMapping
	public LoanDTO createLoan(@RequestBody LoanDTO loanDTO) {
		return LoanMapper.toDto(loanService.createLoan(loanDTO.getUserId(), loanDTO.getBookId()));
	}

	@PostMapping("/{loanId}/return")
	public LoanDTO returnLoan(@PathVariable String loanId) {
		return LoanMapper.toDto(loanService.returnLoan(loanId));
	}

	@GetMapping
	public List<LoanDTO> getAllLoans() {
		return loanService.getAllLoans().stream().map(LoanMapper::toDto).toList();
	}
}
