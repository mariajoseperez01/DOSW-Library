package edu.eci.dosw.tdd.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.eci.dosw.tdd.core.exception.BookNotAvailableException;
import edu.eci.dosw.tdd.core.exception.LoanLimitExceededException;
import edu.eci.dosw.tdd.core.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<ApiError> handleBookNotAvailable(BookNotAvailableException ex) {
        return build(HttpStatus.CONFLICT, "Book not available", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "User not found", ex.getMessage());
    }

    @ExceptionHandler(LoanLimitExceededException.class)
    public ResponseEntity<ApiError> handleLoanLimitExceeded(LoanLimitExceededException ex) {
        return build(HttpStatus.BAD_REQUEST, "Loan limit exceeded", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "Validation error", ex.getMessage());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String error, String message) {
        ApiError payload = new ApiError(LocalDateTime.now(), status.value(), error, message);
        return ResponseEntity.status(status).body(payload);
    }
}
