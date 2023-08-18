package com.sb.products.main.config.security;

import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService service;

	@Autowired
	private UserRepository repository;

	@Override
	protected void doFilterInternal(HttpServletRequest request,  HttpServletResponse response, FilterChain chain)
	  throws ServletException, IOException {
		var token = getToken(request);

		if (token != null) {
			var subject = service.validateToken(token);
			UserDetails user = repository.findByEmail(subject);

			var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		return authHeader == null ? null : authHeader.replace("Bearer ", "");
	}
}
