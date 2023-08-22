package com.sb.products.data.usecases.user;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;
import com.sb.products.domain.factories.UserFactory;

public class UpdateUserUseCase {

	private final UserGateway userGateway;

	public UpdateUserUseCase(UserGateway userGateway) {
		this.userGateway = userGateway;
	}

	public Output execute(Input input) throws RequiredException, NotFoundException, UnauthorizedException {
		var user = UserFactory.create(
		  input.id(),
		  input.fullName(),
		  input.password(),
		  input.isEnabled(),
		  input.isAccountNonLocked(),
		  input.isCredentialsNonExpired(),
		  input.isAccountNonExpired()
		);

		var result = userGateway.update(input.id(), user);

		return new Output(result);
	}

	public record Input(
	  String id,
	  String fullName,
	  String password,
	  boolean isEnabled,
	  boolean isAccountNonLocked,
	  boolean isCredentialsNonExpired,
	  boolean isAccountNonExpired
	) {}

	public record Output(User user) {}
}
