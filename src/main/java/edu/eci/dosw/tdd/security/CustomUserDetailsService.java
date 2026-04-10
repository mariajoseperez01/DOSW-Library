package edu.eci.dosw.tdd.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.eci.dosw.tdd.persistence.port.UserRepositoryPort;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepositoryPort userRepository;
	private final JwtService jwtService;

	public CustomUserDetailsService(UserRepositoryPort userRepository, JwtService jwtService) {
		this.userRepository = userRepository;
		this.jwtService = jwtService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userDao = userRepository.findByName(username)
			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return new UserPrincipal(
			userDao.getId(),
			userDao.getName(),
			userDao.getPassword(),
			userDao.getRole(),
			jwtService.toAuthorities(userDao.getRole().name()));
	}
}
