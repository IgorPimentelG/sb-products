package com.sb.products.data.usecases.user;

import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllUsersUseCase {

	private final UserGateway userGateway;

	public FindAllUsersUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public Output execute(Input input) {
		var users = userGateway.findAll(input.pageable());

		return new Output(users);
	}

	public record Input(Pageable pageable) {}

	public record Output(Page<User> users) {}
}
