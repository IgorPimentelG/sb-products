package com.sb.products.data.usecases.auth;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.domain.entities.User;
import com.sb.products.domain.errors.DuplicatePermissionException;
import com.sb.products.domain.factories.UserFactory;

public class RegisterUseCase {

	private final AuthGateway authGateway;

	public RegisterUseCase(AuthGateway authGateway) {
		this.authGateway = authGateway;
	}

	public Output execute(Input input)
	  throws ConflictException, RequiredException, DuplicatePermissionException {
		var user = UserFactory.create(
		  input.fullName(),
		  input.email(),
		  input.password()
		);

		var createdUser = authGateway.signUp(user, input.role());

		return new Output(createdUser);
	}

	public record Input(String fullName, String email, String password, String role) {}

	public record Output(User user) {}
}
