package com.sb.products.data.gateway.factories;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.outputs.AuthOutput;
import com.sb.products.data.usecases.auth.AuthenticateUseCase;
import com.sb.products.data.usecases.auth.RegisterUseCase;
import com.sb.products.domain.entities.User;

public abstract class AuthGatewayFactory {

	public static AuthGateway create(AuthGateway authGateway) {
		return new Output(
		  new AuthenticateUseCase(authGateway),
		  new RegisterUseCase(authGateway)
		);
	}

	public record Output(AuthenticateUseCase signInUseCase, RegisterUseCase registerUseCase)
	  implements AuthGateway {

		@Override
		public AuthOutput signIn(String email, String password)
		  throws UnauthorizedException {
			return signInUseCase.execute(new AuthenticateUseCase.Input(email, password)).credentials();
		}

		@Override
		public User signUp(User user, String role)
		  throws ConflictException, RequiredException {
			return registerUseCase.execute(new RegisterUseCase.Input(
			  user.getFullName(),
			  user.getEmail(),
			  user.getPassword(),
			  role
			)).user();
		}
	}
}
