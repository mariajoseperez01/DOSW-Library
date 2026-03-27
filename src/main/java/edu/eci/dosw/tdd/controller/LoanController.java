package edu.eci.dosw.tdd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import edu.eci.dosw.tdd.controller.dto.LoanDTO;
import edu.eci.dosw.tdd.controller.mapper.LoanMapper;
import edu.eci.dosw.tdd.core.service.LoanService;

@RestController
@RequestMapping("/loans")
@Tag(name = "Loans", description = "Operaciones para gestionar prestamos")
public class LoanController {

	private final LoanService loanService;

	public LoanController(LoanService loanService) {
		this.loanService = loanService;
	}

	@PostMapping
	@Operation(summary = "Crear prestamo", description = "Crea un prestamo para un usuario y libro")
	public LoanDTO createLoan(@RequestBody LoanDTO loanDTO) {
		return LoanMapper.toDto(loanService.createLoan(loanDTO.getUserId(), loanDTO.getBookId()));
	}

	@PostMapping("/{loanId}/return")
	@Operation(summary = "Devolver prestamo", description = "Marca un prestamo como devuelto")
	public LoanDTO returnLoan(@PathVariable String loanId) {
		return LoanMapper.toDto(loanService.returnLoan(loanId));
	}

	@GetMapping
	@Operation(summary = "Listar prestamos", description = "Obtiene todos los prestamos")
	public List<LoanDTO> getAllLoans() {
		return loanService.getAllLoans().stream().map(LoanMapper::toDto).toList();
	}
}
