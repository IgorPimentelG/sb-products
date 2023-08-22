package com.sb.products.infra.controller;

import com.sb.products.infra.AbstractIntegration;
import com.sb.products.infra.TestConstants;
import com.sb.products.infra.controller.dtos.AuthDto;
import com.sb.products.infra.controller.dtos.CredentialsDto;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserUpdateDto;
import com.sb.products.main.Application;
import com.sb.products.main.config.handles.ExceptionResponse;
import com.sb.products.mocks.MockUser;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
  classes = Application.class
)
@TestMethodOrder(OrderAnnotation.class)
public class UserControllerTest extends AbstractIntegration {

	static UserDto user;
	static MockUser mock;
	static RequestSpecification specification;

	@BeforeAll
	static void setup() {
		mock = new MockUser();
	}

	@Test
	@Order(0)
	@DisplayName("should authenticate user")
	void authentication() {
		CredentialsDto credentials = new CredentialsDto("admin@sb.com", "12345678");

		var result = given()
		  .basePath("/api/auth/signin")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(credentials)
		  .when()
		  .post()
		  .then()
		  .statusCode(200)
		  .extract().body().as(AuthDto.class);

		user = result.user();

		specification = new RequestSpecBuilder()
		  .addHeader("Authorization", "Bearer " + result.token().accessToken())
		  .setBasePath("/api/user/v1")
		  .setPort(TestConstants.SERVER_PORT)
		  .addFilter(new RequestLoggingFilter(LogDetail.ALL))
		  .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		  .build();
	}

	@Test
	@Order(1)
	@DisplayName("should find a user")
	void testFindUser() {
		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", user.id)
		  .when()
		  .get("{id}")
		  .then()
		  .statusCode(200)
		  .extract().body();

		assertNotNull(result);
	}

	@Test
	@Order(2)
	@DisplayName("shouldn't find a user when it doesn't exists")
	void testFindUserThatDoesNotExists() {
		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", "non-existent-id")
		  .when()
		  .get("{id}")
		  .then()
		  .statusCode(404)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Resource with id (non-existent-id) not exists."));
	}

	@Test
	@Order(3)
	@DisplayName("should list all users")
	void testListAllUsers() {
		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .when()
		  .get()
		  .then()
		  .statusCode(200)
		  .extract().body();

		assertNotNull(result);
	}

	@Test
	@Order(4)
	@DisplayName("should update a user")
	void testUpdateUser() {

		var userDto = new UserUpdateDto(
			"Updated Name",
		  "updated_password",
		  true,
		  true,
		  true,
		  true
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", user.id)
		  .body(userDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(200)
		  .extract().body();

		assertNotNull(result);
	}

	@Test
	@Order(6)
	@DisplayName("shouldn't update a user when it doesn't exists")
	void testUpdateUserThatDoesNotExists() {

		var userDto = new UserUpdateDto(
		  "Updated Name",
		  "updated_password",
		  true,
		  true,
		  true,
		  true
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", "non-existent-id")
		  .body(userDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(404)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Resource with id (non-existent-id) not exists."));
	}

	@Test
	@Order(7)
	@DisplayName("shouldn't update user with invalid fields")
	void testUpdateAProductWithInvalidFields() {
		var userDto = new UserUpdateDto(
		  "",
		  "",
		  true,
		  true,
		  true,
		  true
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", user.id)
		  .body(userDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(400)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Invalid field"));
	}

	@Test
	@Order(8)
	@DisplayName("should delete a user")
	void testDeleteUser() {
		given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", user.id)
		  .when()
		  .delete("{id}")
		  .then()
		  .statusCode(204);
	}
}