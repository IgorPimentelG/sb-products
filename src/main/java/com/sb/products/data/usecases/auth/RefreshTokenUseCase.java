package com.sb.products.data.usecases.auth;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.outputs.AuthOutput;

public class RefreshTokenUseCase {

	private final AuthGateway authGateway;

	public RefreshTokenUseCase(AuthGateway authGateway) {
		this.authGateway = authGateway;
	}

	public Output execute(Input input) throws UnauthorizedException, NotFoundException {
		var credentials = authGateway.refreshToken(input.email(), input.refreshToken());

		if (!credentials.user().isEnabled()) {
			throw new UnauthorizedException();
		}

		return new Output(credentials);
	}

	public record Input(String email, String refreshToken) {}

	public record Output(AuthOutput credentials) {}
}
