package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class UserService implements UserGateway {

	@Autowired
	private UserRepository repository;

	private final UserMapper mapper = UserMapper.INSTANCE;
	private final Logger logger = Logger.getLogger(UserService.class.getName());

	@Override
	public User update(String id, User user) throws NotFoundException, RequiredException, UnauthorizedException {
		if (user == null) {
			logger.log(Level.WARNING, "[V1] User cannot be null.");
			throw new RequiredException();
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		var authenticatedUser = repository.findByEmail(auth.getName());

		var entitySchema = repository.findById(id).orElseThrow(() -> {
			logger.log(Level.WARNING, "[V1] User not found.");
			return new NotFoundException(id);
		});

		if (!authenticatedUser.getId().equals(entitySchema.getId())) {
			throw new UnauthorizedException();
		}

		if (user.getPassword() != null) {
			entitySchema.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		}

		if (user.getFullName() != null) {
			entitySchema.setFullName(user.getFullName());
		}

		if (user.isEnabled() && user.isAccountNonExpired() && user.isAccountNonLocked()
		  && user.isCredentialsNonExpired()) {
			entitySchema.setEnabled(user.isEnabled());
			entitySchema.setAccountNonExpired(user.isAccountNonExpired());
			entitySchema.setAccountNonLocked(user.isAccountNonLocked());
			entitySchema.setCredentialsNonExpired(user.isCredentialsNonExpired());
		}
		repository.save(entitySchema);

		logger.log(Level.INFO, "[V1] User updated.");

		return mapper.toEntity(entitySchema);
	}

	@Override
	public User findById(String id) throws NotFoundException {
		var entitySchema = repository.findById(id).orElseThrow(() -> {
			logger.log(Level.WARNING, "[V1] User not found.");
			return new NotFoundException(id);
		});

		logger.log(Level.INFO, "[V1] Find user.");

		return mapper.toEntity(entitySchema);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		logger.log(Level.INFO, "[V1] Find all users.");

		var users = repository.findAll(pageable);
		var entityList = mapper.toListEntity(users.stream().toList());

		return new PageImpl<>(entityList);
	}

	@Override
	public void delete(String id) throws NotFoundException {
		var entitySchema = repository.findById(id).orElseThrow(() -> {
			logger.log(Level.WARNING, "[V1] User not found.");
			return new NotFoundException(id);
		});

		repository.delete(entitySchema);
		logger.log(Level.INFO, "[V1] User deleted.");
	}

	@Transactional
	@Override
	public User disable(String id) throws NotFoundException, UnauthorizedException {
		repository.disableUser(id);

		var entitySchema = repository.findById(id).orElseThrow(() -> {
			logger.log(Level.WARNING, "[V1] User not found.");
			return new NotFoundException(id);
		});

		logger.log(Level.INFO, "[V1] User disabled.");

		return mapper.toEntity(entitySchema);
	}
}
