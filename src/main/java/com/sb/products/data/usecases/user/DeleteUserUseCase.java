package com.sb.products.data.usecases.user;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.UserGateway;

public class DeleteUserUseCase {

	private final UserGateway userGateway;

	public DeleteUserUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public void execute(Input input) throws NotFoundException {
		userGateway.delete(input.id);
	}

	public record Input(String id) {}
}
