package com.sb.products.infra.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sb.products.infra.database.schemas.UserSchema;
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

	public String generateToken(UserSchema user) {
		return JWT.create()
		  .withClaim("roles", user.getRoles())
		  .withIssuer(getIssuerUrl())
		  .withSubject(user.getEmail())
		  .withExpiresAt(getExpirationDate())
		  .sign(getAlgorithm());
	}

	public String validateToken(String token) {
		return JWT.require(getAlgorithm())
		  .withIssuer(getIssuerUrl())
		  .build()
		  .verify(token)
		  .getSubject();
	}

	public Date getExpirationDate() {
		var now = new Date();
		return new Date(now.getTime() + validatyInMilliseconds);
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
