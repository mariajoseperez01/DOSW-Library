package edu.eci.dosw.tdd.core.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import edu.eci.dosw.tdd.core.model.User;
import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;
import edu.eci.dosw.tdd.security.JwtService;
import edu.eci.dosw.tdd.security.dto.LoginRequest;
import edu.eci.dosw.tdd.security.dto.LoginResponse;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepositoryPort userRepository;
	private final JwtService jwtService;

	public AuthService(AuthenticationManager authenticationManager, UserRepositoryPort userRepository, JwtService jwtService) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	public LoginResponse login(LoginRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userRepository.findByName(request.getUsername())
			.orElseThrow();

		String token = jwtService.generateToken(user.getId(), user.getName(), user.getRole().name());
		return new LoginResponse(token);
	}
}
