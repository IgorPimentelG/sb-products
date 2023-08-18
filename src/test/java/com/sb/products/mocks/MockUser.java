package com.sb.products.mocks;

import com.sb.products.domain.entities.User;
import com.sb.products.domain.factories.UserFactory;
import com.sb.products.infra.controller.dtos.ProductDto;
import com.sb.products.infra.database.schemas.UserSchema;

import java.util.ArrayList;
import java.util.List;

public class MockUser {

	public User createEntity() {
		return createEntity(0);
	}

	public UserSchema createEntitySchema() {
		return createEntitySchema(0);
	}

	public ProductDto createDto() {
		return new ProductDto(
		  "Any Name",
		  "Any Description",
		  2.500D,
		  "594958"
		);
	}

	public List<User> createListEntity() {
		List<User> users = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			users.add(createEntity(i));
		}

		return users;
	}

	public List<UserSchema> createListEntitySchema() {
		List<UserSchema> users = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			users.add(createEntitySchema(i));
		}

		return users;
	}

	public User createEntity(int number) {
		return UserFactory.create(
		  String.valueOf(number),
		  "any name",
		  "any@mail.com",
		  "any_password"
		);
	}

	public UserSchema createEntitySchema(int number) {
		return new UserSchema(
		  String.valueOf(number),
		  "any name",
		  "any@mail.com",
		  "any_password",
		  true,
		  true,
		  true,
		  true
		);
	}
}
