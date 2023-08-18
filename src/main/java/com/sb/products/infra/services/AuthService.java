package com.sb.products.infra.services;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.outputs.AuthOutput;
import com.sb.products.data.gateway.outputs.TokenOutput;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.database.repositories.PermissionRepository;
import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.database.schemas.UserSchema;
import com.sb.products.infra.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService implements UserDetailsService, AuthGateway {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TokenService tokenService;

	@Autowired
	@Lazy
	private AuthenticationManager authManger;

	@Autowired
	private PermissionRepository permissionRepository;

	private final UserMapper mapper = UserMapper.INSTANCE;

	@Override
	public AuthOutput signIn(String email, String password) throws UnauthorizedException {
		try {
			var user = new UsernamePasswordAuthenticationToken(email, password);
			var auth = authManger.authenticate(user);
			var token = tokenService.generateToken((UserSchema) auth.getPrincipal());

			var authenticatedUser = userRepository.findByEmail(email);

			var tokenOutput = new TokenOutput(
				token,
				new Date(),
				tokenService.getExpirationDate()
			);

			return new AuthOutput(mapper.toEntity(authenticatedUser), tokenOutput);
		} catch (AuthenticationException e) {
			throw new UnauthorizedException();
		}
	}

	@Override
	public User signUp(User user, String role) throws ConflictException, RequiredException {
		if (user == null) {
			throw new RequiredException();
		}

		String encryptPassword = new BCryptPasswordEncoder().encode(user.getPassword());

		var emailExists = userRepository.findByEmail(user.getEmail());

		if (emailExists != null) {
			throw new ConflictException("Email already exists.");
		}

		var permission = permissionRepository.findByRole(role);

		var userSchema = mapper.toSchema(user);
		userSchema.setPassword(encryptPassword);
		userSchema.addPermission(permission);

		var createdUser = userRepository.save(userSchema);

		return mapper.toEntity(createdUser);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findByEmail(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not exists.");
		}

		return user;
	}
}
