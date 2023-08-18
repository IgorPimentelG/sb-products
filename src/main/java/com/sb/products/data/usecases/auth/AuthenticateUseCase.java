package com.sb.products.data.usecases.auth;

import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.outputs.AuthOutput;

public class AuthenticateUseCase {

	private final AuthGateway authGateway;

	public AuthenticateUseCase(AuthGateway authGateway) {
		this.authGateway = authGateway;
	}

	public Output execute(Input input) throws UnauthorizedException {
		var credentials = authGateway.signIn(input.email(), input.password());

		if (!credentials.user().isEnabled()) {
			throw new UnauthorizedException();
		}

		return new Output(credentials);
	}

	public record Input(String email, String password) {}

	public record Output(AuthOutput credentials) {}
}
