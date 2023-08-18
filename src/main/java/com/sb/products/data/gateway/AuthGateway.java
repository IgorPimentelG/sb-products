package com.sb.products.data.gateway;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.outputs.AuthOutput;
import com.sb.products.domain.entities.User;

public interface AuthGateway {
	AuthOutput signIn(String email, String password) throws UnauthorizedException;

	User signUp(User user, String role) throws ConflictException, RequiredException;
}