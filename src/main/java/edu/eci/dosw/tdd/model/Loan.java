package edu.eci.dosw.tdd.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Loan {
    Book book;
    User user;
    LocalDate loanDate;
    Status status;
    LocalDate returnDate;
}
