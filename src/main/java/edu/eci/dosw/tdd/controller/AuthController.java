package edu.eci.dosw.tdd.controller;

import edu.eci.dosw.tdd.core.service.AuthService;
import edu.eci.dosw.tdd.security.dto.LoginRequest;
import edu.eci.dosw.tdd.security.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public LoginResponse login(@RequestBody LoginRequest request) {
		return authService.login(request);
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String handleBadCredentials() {
		return "{\"error\":\"Invalid credentials\"}";
	}
}
