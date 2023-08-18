package com.sb.products.data.usecases.user;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;

public class FindUserUseCase {

	private final UserGateway userGateway;

	public FindUserUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public Output execute(Input input) throws NotFoundException {
		var user = userGateway.findById(input.id());

		return new Output(user);
	}

	public record Input(String id) {}

	public record Output(User user) {}
}
