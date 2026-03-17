package edu.eci.dosw.tdd.controller;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, int status, String error, String message) {
}
