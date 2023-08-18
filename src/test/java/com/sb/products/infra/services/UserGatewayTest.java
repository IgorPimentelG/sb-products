package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.database.schemas.UserSchema;
import com.sb.products.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserGatewayTest {
	MockUser mock;

	@InjectMocks
	private UserService gateway;

	@Mock
	private UserRepository repository;

	@BeforeEach
	public void setup() {
		mock = new MockUser();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void shouldUpdateProduct() throws Exception {
		User user = mock.createEntity();
		UserSchema userSchema =  mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(userSchema));

		var result = gateway.update("Any Identifier", user);

		verify(repository, times(1)).findById(any());
		verify(repository, times(1)).save(any());
		assertNotNull(result);
		assertEquals(result.getId(), userSchema.getId());
		assertEquals(result.getFullName(), userSchema.getFullName());
		assertEquals(result.getEmail(), userSchema.getEmail());
	}

	@Test
	public void shouldThrowsNotFoundExceptionWhenUpdateNonExistentProduct() {
		User user =  mock.createEntity();

		when(repository.findById(any())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			gateway.update("Any Identifier", user);
		});

		String expectedMessage = "Resource with id (Any Identifier) not exists.";
		String resultMessage = exception.getMessage();

		verify(repository, times(1)).findById(any());
		verify(repository, times(0)).save(any());
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void shouldThrowsRequiredExceptionWhenUpdateWithNullUser() {
		Exception exception = assertThrows(RequiredException.class, () -> {
			gateway.update("Any Identifier", null);
		});

		String expectedMessage = "Resource cannot be null.";
		String resultMessage = exception.getMessage();

		verify(repository, times(0)).findById(any());
		verify(repository, times(0)).save(any());
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void shouldFindProduct() throws Exception {
		UserSchema user = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(user));

		var result = gateway.findById(user.getId());

		verify(repository, times(1)).findById(any());
		assertNotNull(result);
		assertEquals(user.getFullName(), result.getFullName());
		assertEquals(user.getEmail(), result.getEmail());
	}

	@Test
	public void shouldThrowsNotFoundExceptionWhenFindNonExistentUser() {
		when(repository.findById(any())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			gateway.findById("Any Identifier");
		});

		String expectedMessage = "Resource with id (Any Identifier) not exists.";
		String resultMessage = exception.getMessage();

		verify(repository, times(1)).findById(any());
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	public void shouldFindAllUsers() {
		Page<UserSchema> users = new PageImpl<>(mock.createListEntitySchema());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(users);

		var result = gateway.findAll(pageable);

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(users.getContent().size(), result.getContent().size());
	}

	@Test
	public void shouldFindAllWithEmptyUsers() {
		Page<UserSchema> users = new PageImpl<>(new ArrayList<>());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(users);

		var result = gateway.findAll(pageable);

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(result.getContent().size(), 0);
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		UserSchema userSchema = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(userSchema));

		gateway.delete("Any Identifier");

		verify(repository, times(1)).delete(any());
	}

	@Test
	public void shouldThrowsNotFoundExceptionWhenDeleteNonExistentUser() {
		when(repository.findById(any())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			gateway.delete("Any Identifier");
		});

		String expectedMessage = "Resource with id (Any Identifier) not exists.";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
		verify(repository, times(0)).delete(any());
	}
}
