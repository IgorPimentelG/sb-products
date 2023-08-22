package com.sb.products.data.gateway.factories;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.data.usecases.user.DeleteUserUseCase;
import com.sb.products.data.usecases.user.FindAllUsersUseCase;
import com.sb.products.data.usecases.user.FindUserUseCase;
import com.sb.products.data.usecases.user.UpdateUserUseCase;
import com.sb.products.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class UserGatewayFactory {

	public static UserGateway create(UserGateway userGateway) {
		return new Output(
		  new UpdateUserUseCase(userGateway),
		  new FindUserUseCase(userGateway),
		  new FindAllUsersUseCase(userGateway),
		  new DeleteUserUseCase(userGateway)
		);
	}

	public record Output(
	  UpdateUserUseCase updateUseCase,
	  FindUserUseCase findUseCase,
	  FindAllUsersUseCase findAllUseCase,
	  DeleteUserUseCase deleteUseCase
	) implements UserGateway {
		@Override
		public User update(String id, User user)
		  throws RequiredException, NotFoundException, UnauthorizedException {
			return updateUseCase.execute(new UpdateUserUseCase.Input(
			  id,
			  user.getFullName(),
			  user.getPassword(),
			  user.isEnabled(),
			  user.isAccountNonLocked(),
			  user.isCredentialsNonExpired(),
			  user.isAccountNonExpired()
			)).user();
		}

		@Override
		public User findById(String id) throws NotFoundException {
			return findUseCase.execute(new FindUserUseCase.Input(id))
			  .user();
		}

		@Override
		public Page<User> findAll(Pageable pageable) {
			return findAllUseCase.execute(new FindAllUsersUseCase.Input(pageable))
			  .users();
		}

		@Override
		public void delete(String id) throws NotFoundException {
			deleteUseCase.execute(new DeleteUserUseCase.Input(id));
		}
	}
}
