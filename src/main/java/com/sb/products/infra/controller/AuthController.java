package com.sb.products.infra.controller;

import com.sb.products.data.errors.ConflictException;
import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.AuthGateway;
import com.sb.products.data.gateway.factories.AuthGatewayFactory;
import com.sb.products.data.gateway.outputs.AuthOutput;
import com.sb.products.domain.entities.Permission;
import com.sb.products.domain.errors.DuplicatePermissionException;
import com.sb.products.infra.controller.docs.auth.CommonRegisterDoc;
import com.sb.products.infra.controller.docs.auth.ManagerRegisterDoc;
import com.sb.products.infra.controller.docs.auth.RefreshTokenDoc;
import com.sb.products.infra.controller.docs.auth.SignInDoc;
import com.sb.products.infra.controller.dtos.AuthDto;
import com.sb.products.infra.controller.dtos.CredentialsDto;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserRegisterDto;
import com.sb.products.infra.mapper.PermissionMapper;
import com.sb.products.infra.mapper.UserMapper;
import com.sb.products.infra.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Endpoints for managing authentication")
public class AuthController {

	private final AuthGateway gateway;
	private final UserMapper mapper = UserMapper.INSTANCE;

	@Autowired
	public AuthController(AuthService authService) {
		this.gateway = AuthGatewayFactory.create(authService);
	}

	@SignInDoc
	@PostMapping(value = "/signin")
	public ResponseEntity<AuthDto> signin(@RequestBody @Valid CredentialsDto credentials)
	  throws UnauthorizedException {

		var auth = gateway.signIn(credentials.email(), credentials.password());

		return ResponseEntity.status(HttpStatus.OK).body(getAuthResponse(auth));
	}

	@ManagerRegisterDoc
	@PostMapping(value = "/manager/signup")
	public ResponseEntity<UserDto> signupManager(@RequestBody @Valid UserRegisterDto userDto)
	  throws ConflictException, RequiredException, DuplicatePermissionException {

		var user = gateway.signUp(
		  mapper.toEntity(userDto),
		  PermissionMapper.MANAGER.name()
		);

		UserDto body = mapper.toDto(user);
		body.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@CommonRegisterDoc
	@PostMapping(value = "/common/signup")
	public ResponseEntity<UserDto> signupCommon(@RequestBody @Valid UserRegisterDto userDto)
	  throws ConflictException, RequiredException, DuplicatePermissionException {

		var user = gateway.signUp(
			mapper.toEntity(userDto), 
			PermissionMapper.COMMON_USER.name()
		);

		UserDto body = mapper.toDto(user);
		body.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@RefreshTokenDoc
	@PutMapping(value = "/refresh/{email}")
	public ResponseEntity<AuthDto> refreshToken(
	  @PathVariable("email") String email,
	  @RequestHeader("Authorization") String refreshToken
	) throws NotFoundException, UnauthorizedException {

		if (refreshToken == null || refreshToken.isBlank() || refreshToken.isEmpty()) {
			throw new UnauthorizedException();
		}

		var auth = gateway.refreshToken(email, refreshToken);

		return ResponseEntity.status(HttpStatus.OK).body(getAuthResponse(auth));
	}

	private AuthDto getAuthResponse(AuthOutput auth) {
		var user = auth.user();
		UserDto body = mapper.toDto(user);
		body.permissions = user.getPermissions().stream().map(Permission::getRole).toList();

		return new AuthDto(body, auth.token());
	}
}
