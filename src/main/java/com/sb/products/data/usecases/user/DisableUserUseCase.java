package com.sb.products.data.usecases.user;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;

public class DisableUserUseCase {

	private final UserGateway userGateway;

	public DisableUserUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public Output execute(Input input) throws NotFoundException, UnauthorizedException {
		var result = userGateway.disable(input.id());
		return new Output(result);
	}

	public record Input(String id) {}

	public record Output(User user) {}
}
