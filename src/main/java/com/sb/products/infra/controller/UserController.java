package com.sb.products.infra.controller;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.UserGateway;
import com.sb.products.data.gateway.factories.UserGatewayFactory;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.controller.dtos.UserUpdateDto;
import com.sb.products.infra.database.schemas.UserSchema;
import com.sb.products.infra.mapper.UserMapper;
import com.sb.products.infra.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserGateway gateway;
	private final UserMapper mapper = UserMapper.INSTANCE;

	@Autowired
	public UserController(UserService databaseGateway) {
		this.gateway = UserGatewayFactory.create(databaseGateway);
	}

	@PutMapping(value = "/v1/{id}")
	public ResponseEntity<UserSchema> update(
	  @PathVariable("id") String id,
	  @RequestBody @Valid UserUpdateDto user,
	  Pageable pageable) throws NotFoundException, RequiredException {
		var entity = gateway.update(id, mapper.toEntity(user));
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(UserController.class).findById(id, pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<UserSchema> findById(@PathVariable("id") String id, Pageable pageable)
	  throws NotFoundException {
		var entity = gateway.findById(id);
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(UserController.class).findAll(pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@GetMapping(value = "/v1")
	public ResponseEntity<Page<UserSchema>> findAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<User> users = gateway.findAll(pageable);
		var listSchema = mapper.toListSchema(users.stream().toList());
		listSchema.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(UserController.class).findById(item.getId(), pageable)).withSelfRel()
				);
			} catch (Exception ignored) {
			}
		});

		return ResponseEntity.status(HttpStatus.OK).body(new PageImpl<>(listSchema));
	}

	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) throws NotFoundException {
		gateway.delete(id);
		return ResponseEntity.noContent().build();
	}
}