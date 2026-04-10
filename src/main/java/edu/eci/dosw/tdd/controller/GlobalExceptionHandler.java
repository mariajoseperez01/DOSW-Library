package edu.eci.dosw.tdd.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleNotFound(NoSuchElementException ex) {
		return Map.of("error", ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> handleUserNotFound(UserNotFoundException ex) {
		return Map.of("error", ex.getMessage());
	}

	@ExceptionHandler(BookNotAvailableException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> handleBookUnavailable(BookNotAvailableException ex) {
		return Map.of("error", ex.getMessage());
	}

	@ExceptionHandler(LoanLimitExceededException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Map<String, String> handleLoanLimitExceeded(LoanLimitExceededException ex) {
		return Map.of("error", ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> handleBadRequest(IllegalArgumentException ex) {
		return Map.of("error", ex.getMessage());
	}
}
