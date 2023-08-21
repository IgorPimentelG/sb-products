package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
	public User update(String id, User user) throws NotFoundException, RequiredException {
		if (user == null) {
			logger.log(Level.WARNING, "[V1] User cannot be null.");
			throw new RequiredException();
		}

		var entitySchema = repository.findById(id).orElseThrow(() -> {
			logger.log(Level.WARNING, "[V1] User not found.");
			return new NotFoundException(id);
		});

		BeanUtils.copyProperties(user, entitySchema);
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
}
