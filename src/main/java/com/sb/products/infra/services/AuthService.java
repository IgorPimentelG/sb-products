package com.sb.products.infra.services;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.outputs.AuthOutput;
import com.sb.products.domain.entities.User;
import com.sb.products.domain.errors.DuplicatePermissionException;
import com.sb.products.infra.mapper.UserMapper;
import com.sb.products.infra.repositories.PermissionRepository;
import com.sb.products.infra.repositories.UserRepository;
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
			var token = tokenService.createToken((User) auth.getPrincipal());

			var authenticatedUser = userRepository.findByEmail(email);

			return new AuthOutput(authenticatedUser, token);
		} catch (AuthenticationException e) {
			throw new UnauthorizedException();
		}
	}

	@Override
	public User signUp(User user, String role)
	  throws ConflictException, RequiredException, DuplicatePermissionException {
		if (user == null) {
			throw new RequiredException();
		}

		String encryptPassword = new BCryptPasswordEncoder().encode(user.getPassword());

		var emailExists = userRepository.findByEmail(user.getEmail());

		if (emailExists != null) {
			throw new ConflictException("Email already exists.");
		}

		var permission = permissionRepository.findByRole(role);

		user.setPassword(encryptPassword);
		user.addPermission(permission);

		return userRepository.save(user);
	}

	@Override
	public AuthOutput refreshToken(String email, String refreshToken) throws NotFoundException {
		var user = userRepository.findByEmail(email);

		if (user == null) {
			throw new NotFoundException(email);
		}

		var token = tokenService.refreshToken(refreshToken);

		return new AuthOutput(user, token);
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
