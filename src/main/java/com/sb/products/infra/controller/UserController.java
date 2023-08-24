package com.sb.products.infra.controller;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.data.gateway.factories.UserGatewayFactory;
import com.sb.products.domain.entities.User;
import com.sb.products.domain.factories.UserFactory;
import com.sb.products.infra.controller.docs.user.*;
import com.sb.products.infra.controller.dtos.UserUpdateDto;
import com.sb.products.infra.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

	private final UserGateway gateway;

	@Autowired
	public UserController(UserService databaseGateway) {
		this.gateway = UserGatewayFactory.create(databaseGateway);
	}

	@UpdateDoc
	@PutMapping(value = "/v1/{id}")
	public ResponseEntity<User> update(
	  @PathVariable("id") String id,
	  @RequestBody @Valid UserUpdateDto user,
	  Pageable pageable) throws NotFoundException, RequiredException, UnauthorizedException {

		var userData = UserFactory.create(
		  id,
		  user.fullName(),
		  user.password(),
		  user.enabled(),
		  user.accountNonLocked(),
		  user.credentialsNonExpired(),
		  user.accountNonExpired()
		);

		var entity = gateway.update(id, userData);
		entity.add(
		  linkTo(
			methodOn(UserController.class).findById(id, pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}

	@FindByIdDoc
	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<User> findById(@PathVariable("id") String id, Pageable pageable)
	  throws NotFoundException {
		var entity = gateway.findById(id);
		entity.add(
		  linkTo(
			methodOn(UserController.class).findAll(pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}

	@FindAllDoc
	@GetMapping(value = "/v1")
	public ResponseEntity<Page<User>> findAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<User> users = gateway.findAll(pageable);
		users.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(UserController.class).findById(item.getId(), pageable)).withSelfRel()
				);
			} catch (Exception ignored) {
			}
		});

		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@DeleteDoc
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) throws NotFoundException {
		gateway.delete(id);
		return ResponseEntity.noContent().build();
	}

	@DisableDoc
	@PatchMapping(value = "/v1/{id}")
	public ResponseEntity<User> disable(@PathVariable("id") String id, Pageable pageable)
	  throws NotFoundException, UnauthorizedException {
		var entity = gateway.disable(id);
		entity.add(
		  linkTo(
			methodOn(UserController.class).findById(id, pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}
}