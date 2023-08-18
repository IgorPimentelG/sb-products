package com.sb.products.domain.entities;

import com.sb.products.domain.errors.DuplicatePermissionException;
import com.sb.products.domain.factories.UserFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

	@Test
	void shouldCreateUser() {
		var id = "any id";
		var fullName = "any full name";
		var email = "any@mail.com";
		var password = "any_password";

		var user = UserFactory.create(id, fullName, email, password);

		assertNotNull(user);
		assertEquals(user.getId(), id);
		assertEquals(user.getFullName(), fullName);
		assertEquals(user.getEmail(), email);
		assertEquals(user.getPassword(), password);
	}


	@Test
	void shouldCreateUserWithStatus() {
		var id = "any id";
		var fullName = "any full name";
		var email = "any@mail.com";
		var password = "any_password";
		var accountNonExpired = true;
		var accountNonLocked = true;
		var credentialsNonExpired = true;
		var enabled = true;

		var user = UserFactory.create(
		  id,
		  fullName,
		  email,
		  password,
		  accountNonExpired,
		  accountNonLocked,
		  credentialsNonExpired,
		  enabled
		);

		assertNotNull(user);
		assertEquals(user.getId(), id);
		assertEquals(user.getFullName(), fullName);
		assertEquals(user.getEmail(), email);
		assertEquals(user.getPassword(), password);
		assertEquals(user.isAccountNonLocked(), accountNonLocked);
		assertEquals(user.isAccountNonExpired(), accountNonExpired);
		assertEquals(user.isCredentialsNonExpired(), credentialsNonExpired);
		assertEquals(user.isEnabled(), enabled);
	}

	@Test
	void shouldAddPermissionInUser() throws DuplicatePermissionException {
		var id = "any id";
		var fullName = "any full name";
		var email = "any@mail.com";
		var password = "any_password";
		var permission = new Permission(1, "any role");

		var user = UserFactory.create(id, fullName, email, password);
		user.addPermission(permission);

		assertNotNull(user);
		assertEquals(user.getPermissions().size(), 1);
		assertEquals(user.getPermissions().get(0).getRole(), "any role");
	}

	@Test
	void shouldThrowsDuplicatePermissionExceptionWhenAddADuplicatePermissionInUser() {
		Exception exception = assertThrows(DuplicatePermissionException.class, () -> {
			var user = UserFactory.create(
			  "any id",
			  "any full name",
			  "any@mail.com",
			  "any_password"
			);
			var permission = new Permission(1, "any role");

			user.addPermission(permission);
			user.addPermission(permission);
		});

		String expectedMessage = "Permission (any role) already exists.";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenUserNameIsEmpty() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			UserFactory.create("any id", "", "any@mail.com", "any_password");
		});

		String expectedMessage = "Full Name cannot be empty";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenEmailIsEmpty() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			UserFactory.create("any id", "any user name", "", "any_password");
		});

		String expectedMessage = "Email cannot be empty";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenPasswordIsEmpty() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			UserFactory.create("any id", "any user name", "any@mail.com", "");
		});

		String expectedMessage = "Password cannot be empty";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenPasswordIsLessThan8Characters() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			UserFactory.create("any id", "any user name", "any@mail.com", "1234567");
		});

		String expectedMessage = "Password cannot be less than 8 characters";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenPasswordHasBlankCharacters() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			UserFactory.create("any id", "any user name", "any@mail.com", "1234 5678");
		});

		String expectedMessage = "Password cannot has empty characters";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}
}
