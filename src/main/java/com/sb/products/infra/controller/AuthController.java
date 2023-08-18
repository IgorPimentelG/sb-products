package com.sb.products.infra.controller;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.factories.AuthGatewayFactory;
import com.sb.products.domain.entities.Permission;
import com.sb.products.infra.controller.dtos.AuthDto;
import com.sb.products.infra.controller.dtos.CredentialsDto;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserRegisterDto;
import com.sb.products.infra.mapper.PermissionMapper;
import com.sb.products.infra.mapper.UserMapper;
import com.sb.products.infra.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthGateway gateway;
	private final UserMapper mapper = UserMapper.INSTANCE;

	@Autowired
	public AuthController(AuthService authService) {
		this.gateway = AuthGatewayFactory.create(authService);
	}

	@PostMapping(value = "/signin")
	public ResponseEntity<AuthDto> signin(@RequestBody @Valid CredentialsDto credentials)
	  throws UnauthorizedException {

		var auth = gateway.signIn(credentials.email(), credentials.password());
		var user = auth.user();

		UserDto userBody = mapper.toDto(user);
		userBody.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return ResponseEntity.status(HttpStatus.OK).body(new AuthDto(userBody, auth.token()));
	}

	@PostMapping(value = "/manager/signup")
	public ResponseEntity<UserDto> signupManager(@RequestBody @Valid UserRegisterDto userDto)
	  throws ConflictException, RequiredException {

		var user = gateway.signUp(
		  mapper.toEntity(userDto),
		  PermissionMapper.MANAGER.name()
		);

		UserDto body = mapper.toDto(user);
		body.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@PostMapping(value = "/common/signup")
	public ResponseEntity<UserDto> signupCommon(@RequestBody @Valid UserRegisterDto userDto)
	  throws ConflictException, RequiredException {

		var user = gateway.signUp(
			mapper.toEntity(userDto), 
			PermissionMapper.COMMON_USER.name()
		);

		UserDto body = mapper.toDto(user);
		body.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}
}
