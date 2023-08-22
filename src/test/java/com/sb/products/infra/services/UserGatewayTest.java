package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.domain.entities.User;
import com.sb.products.infra.database.repositories.UserRepository;
import com.sb.products.infra.database.schemas.UserSchema;
import com.sb.products.mocks.MockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
	@DisplayName("should update a user")
	public void testUpdateUser() throws Exception {
		User user = mock.createEntity();
		UserSchema userSchema =  mock.createEntitySchema();

		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);

		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(repository.findByEmail(any())).thenReturn(userSchema);
		SecurityContextHolder.setContext(securityContext);

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
	@DisplayName("should throws RequiredException when update a user with null data")
	public void testThrowsNotFoundExceptionWhenUpdateNonExistentUser() {
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
	@DisplayName("should throws RequiredException when update a user with null data")
	public void testThrowsRequiredExceptionWhenUpdateWithNullUser() {
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
	@DisplayName("should find a user")
	public void findUser() throws Exception {
		UserSchema user = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(user));

		var result = gateway.findById(user.getId());

		verify(repository, times(1)).findById(any());
		assertNotNull(result);
		assertEquals(user.getFullName(), result.getFullName());
		assertEquals(user.getEmail(), result.getEmail());
	}

	@Test
	@DisplayName("should throws NotFoundException when find non existent user")
	public void testThrowsNotFoundExceptionWhenFindNonExistentUser() {
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
	@DisplayName("should find all users")
	public void testFindAllUsers() {
		Page<UserSchema> users = new PageImpl<>(mock.createListEntitySchema());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(users);

		var result = gateway.findAll(pageable);

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(users.getContent().size(), result.getContent().size());
	}

	@Test
	@DisplayName("should find all users with empty users")
	public void testFindAllWithEmptyUsers() {
		Page<UserSchema> users = new PageImpl<>(new ArrayList<>());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(users);

		var result = gateway.findAll(pageable);

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(result.getContent().size(), 0);
	}

	@Test
	@DisplayName("should delete a user")
	public void testDeleteUser() throws Exception {
		UserSchema userSchema = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(userSchema));

		gateway.delete("Any Identifier");

		verify(repository, times(1)).delete(any());
	}

	@Test
	@DisplayName("should throws NotFoundException when delete a non existent user")
	public void testThrowsNotFoundExceptionWhenDeleteNonExistentUser() {
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