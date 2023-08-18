package com.sb.products.infra.controller.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserRegisterDto(
	@NotBlank(message = "Full Name cannot be empty")
	@NotNull(message = "Full Name cannot be empty")
	@Length(min = 5, max = 100, message = "Full name must be at least 5 to a max of 100 characters")
	String fullName,

	@Email(message = "Invalid email address")
	@NotNull(message = "Email cannot be empty")
    String email,

	@NotNull(message = "Password cannot be empty")
	@Length(min = 8, max = 30, message = "Password must be at least 8 to a max of 30 characters")
	String password
) {}
