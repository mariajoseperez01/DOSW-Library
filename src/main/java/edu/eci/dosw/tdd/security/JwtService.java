package edu.eci.dosw.tdd.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

	private final SecretKey secretKey;
	private final long ttlMillis;

	public JwtService(@Value("${app.security.jwt.secret}") String secret,
			@Value("${app.security.jwt.ttl-millis}") long ttlMillis) {
		this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		this.ttlMillis = ttlMillis;
	}

	public String generateToken(String userId, String username, String role) {
		Instant now = Instant.now();
		return Jwts.builder()
			.subject(username)
			.claim("uid", userId)
			.claim("role", role)
			.issuedAt(Date.from(now))
			.expiration(Date.from(now.plusMillis(ttlMillis)))
			.signWith(secretKey)
			.compact();
	}

	public Claims parseToken(String token) {
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public String getUsername(String token) {
		return parseToken(token).getSubject();
	}

	public String getUserId(String token) {
		return parseToken(token).get("uid", String.class);
	}

	public String getRole(String token) {
		return parseToken(token).get("role", String.class);
	}

	public boolean isValid(String token) {
		try {
			parseToken(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public List<GrantedAuthority> toAuthorities(String role) {
		return List.of(new SimpleGrantedAuthority("ROLE_" + role));
	}
}
