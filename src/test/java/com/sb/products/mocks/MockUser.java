package com.sb.products.mocks;

import com.sb.products.domain.entities.User;
import com.sb.products.domain.factories.UserFactory;

import java.util.ArrayList;
import java.util.List;

public class MockUser {

	public User createEntity() {
		return createEntity(0);
	}

	public List<User> createListEntity() {
		List<User> users = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			users.add(createEntity(i));
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
}