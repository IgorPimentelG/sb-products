package com.sb.products.infra.controller.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserRegisterDto(
	@NotEmpty(message = "Full Name cannot be empty")
	@Length(min = 5, max = 100, message = "Full name must be at least 5 to a max of 100 characters")
	String fullName,

	@Email(message = "Invalid email address")
	@NotEmpty(message = "Email cannot be empty")
    String email,

	@NotEmpty(message = "Password cannot be empty")
	@Length(min = 8, max = 30, message = "Password must be at least 8 to a max of 30 characters")
	String password
) {}
