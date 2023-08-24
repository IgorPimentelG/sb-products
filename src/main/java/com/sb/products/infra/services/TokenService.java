package com.sb.products.infra.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sb.products.data.gateway.outputs.TokenOutput;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;

@Service
public class TokenService {

	@Value("${security.jwt.token.secret-key:my-secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validatyInMilliseconds;

	@Autowired
	private UserRepository userRepository;

	public TokenOutput createToken(User user) {
		Algorithm algorithm = getAlgorithm();
		Date now = new Date();

		var expirationAccessToken = getExpirationAccessToken(now);
		var expirationRefreshToken = getExpirationRefreshToken(now);

		var accessToken = getAccessToken(user, algorithm, expirationAccessToken);
		var refreshToken = getRefreshToken(user, algorithm, now, expirationRefreshToken);

		return new TokenOutput(
		  accessToken,
		  refreshToken,
		  now,
		  expirationAccessToken,
		  expirationRefreshToken
		);
	}

	public TokenOutput refreshToken(String refreshToken) {
		if (refreshToken.contains("Bearer ")) {
			refreshToken = refreshToken.substring("Bearer ".length());
		}

		JWTVerifier verifier = JWT.require(getAlgorithm()).build();
		DecodedJWT decodedJWT = verifier.verify(refreshToken);
		String subject = decodedJWT.getSubject();

		var user = userRepository.findByEmail(subject);

		return createToken(user);
	}

	private String getAccessToken(User user, Algorithm algorithm, Date expiration) {
		return JWT.create()
		  .withClaim("roles", user.getRoles())
		  .withIssuer(getIssuerUrl())
		  .withSubject(user.getEmail())
		  .withExpiresAt(expiration)
		  .sign(algorithm);
	}

	private String getRefreshToken(User user, Algorithm algorithm, Date now, Date expiration) {
		return JWT.create()
		  .withClaim("roles", user.getRoles())
		  .withIssuedAt(now)
		  .withExpiresAt(expiration)
		  .withSubject(user.getEmail())
		  .sign(algorithm)
		  .strip();
	}

	public String validateToken(String token) {
		return JWT.require(getAlgorithm())
		  .withIssuer(getIssuerUrl())
		  .build()
		  .verify(token)
		  .getSubject();
	}

	public Date getExpirationAccessToken(Date now) {
		return new Date(now.getTime() + validatyInMilliseconds);
	}

	public Date getExpirationRefreshToken(Date now) {
		return new Date(now.getTime() + (validatyInMilliseconds * 3));
	}

	private Algorithm getAlgorithm() {
		return Algorithm.HMAC256(secretKey);
	}

	private String getIssuerUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath()
		  .build()
		  .toUriString();
	}
}
