package com.sb.products.main.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
		  .csrf(AbstractHttpConfigurer::disable)
		  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		  .authorizeHttpRequests(authorize -> authorize
		      .requestMatchers(HttpMethod.POST, "/api/product/**").hasAnyRole("MANAGER", "ADMIN")
		      .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasAnyRole("MANAGER", "ADMIN")
		      .requestMatchers(HttpMethod.POST, "/api/auth/manager/signup").hasRole("ADMIN")
		      .requestMatchers(
				  "/api/auth/common/signup",
		          "/api/auth/signin"
		      ).permitAll()
		      .requestMatchers("/user").hasAnyRole("ADMIN")
		      .requestMatchers(
				  "swagger-ui/**",
				  "/v3/api-docs/**",
				  "/api/auth/refresh/**"
		      ).permitAll()
		      .anyRequest().authenticated()
		  )
		  .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
		  .build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
	  throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
