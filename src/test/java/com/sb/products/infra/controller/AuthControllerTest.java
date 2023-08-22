package com.sb.products.infra.controller;

import com.sb.products.infra.AbstractIntegration;
import com.sb.products.infra.TestConstants;
import com.sb.products.infra.controller.dtos.AuthDto;
import com.sb.products.infra.controller.dtos.CredentialsDto;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserRegisterDto;
import com.sb.products.main.Application;
import com.sb.products.main.config.handles.ExceptionResponse;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
  classes = Application.class
)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerTest extends AbstractIntegration {

	@Test
	@Order(0)
	@DisplayName("should register a common user")
	void testRegisterCommonUser() {

		var body = new UserRegisterDto(
		  "Any Full Name",
		  "any_common@mail.com",
		  "any_password"
		);

		var result = given()
		  .basePath("/api/auth/common/signup")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(201)
		  .extract().body().as(UserDto.class);

		assertNotNull(result);
	}

	@Test
	@Order(1)
	@DisplayName("shouldn't register a common user with duplicate email")
	void testRegisterCommonUserWithDuplicateEmail() {

		var body = new UserRegisterDto(
		  "Any Full Name",
		  "any_common@mail.com",
		  "any_password"
		);

		var result = given()
		  .basePath("/api/auth/common/signup")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(409)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertEquals(result.message(), "Email already exists.");
	}

	@Test
	@Order(2)
	@DisplayName("shouldn't register a common user with invalid fields")
	void testRegisterCommonUserWithInvalidFields() {

		var body = new UserRegisterDto(
		  "",
		  "",
		  ""
		);

		var result = given()
		  .basePath("/api/auth/common/signup")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(400)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Invalid fields"));
	}

	@Test
	@Order(3)
	@DisplayName("should register a manager user")
	void testRegisterManagerUser() {
		RequestSpecification specification = loginAdmin();

		var body = new UserRegisterDto(
		  "Any Full Name",
		  "any_manager@mail.com",
		  "any_password"
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(201)
		  .extract().body().as(UserDto.class);

		assertNotNull(result);
	}

	@Test
	@Order(4)
	@DisplayName("shouldn't register a manager user with duplicate email")
	void testRegisterManagerUserWithDuplicateEmail() {

		RequestSpecification specification = loginAdmin();

		var body = new UserRegisterDto(
		  "Any Full Name",
		  "any_manager@mail.com",
		  "any_password"
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(409)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertEquals(result.message(), "Email already exists.");
	}

	@Test
	@Order(5)
	@DisplayName("shouldn't register a manager user with invalid fields")
	void testRegisterManagerUserWithInvalidFields() {

		RequestSpecification specification = loginAdmin();

		var body = new UserRegisterDto(
		  "",
		  "",
		  ""
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(400)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Invalid fields"));
	}

	@Test
	@Order(6)
	@DisplayName("shouldn't register a manager user without is authenticate as admin")
	void testRegisterManagerUserWithoutIsAuthenticateAsAdmin() {

		var body = new UserRegisterDto(
		  "Any Full Name",
		  "any_manager2@mail.com",
		  "any_password"
		);

		given()
		  .basePath("/api/auth/manager/signup")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(body)
		  .when()
		  .post()
		  .then()
		  .statusCode(403);
	}

	@Test
	@Order(7)
	@DisplayName("should refresh token")
	void testRefreshToken() {
		CredentialsDto credentials = new CredentialsDto("admin@sb.com", "12345678");

		var token = given()
		  .basePath("/api/auth/signin")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(credentials)
		  .when()
		  .post()
		  .then()
		  .statusCode(200)
		  .extract().body().as(AuthDto.class).token();

		RequestSpecification specification = new RequestSpecBuilder()
		  .addHeader("Authorization", "Bearer " + token.accessToken())
		  .setBasePath("/api/auth/refresh")
		  .setPort(TestConstants.SERVER_PORT)
		  .addFilter(new RequestLoggingFilter(LogDetail.ALL))
		  .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		  .build();

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("email", "admin@sb.com")
		  .when()
		  .put("{email}")
		  .then()
		  .statusCode(200)
		  .extract().body().as(AuthDto.class);

		assertNotNull(result);
	}

	@Test
	@Order(8)
	@DisplayName("shouldn't refresh token of user that does not exists")
	void testRefreshTokenOfUserThatDoesNotExists() {
		CredentialsDto credentials = new CredentialsDto("admin@sb.com", "12345678");

		var token = given()
		  .basePath("/api/auth/signin")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(credentials)
		  .when()
		  .post()
		  .then()
		  .statusCode(200)
		  .extract().body().as(AuthDto.class).token();

		RequestSpecification specification = new RequestSpecBuilder()
		  .addHeader("Authorization", "Bearer " + token.accessToken())
		  .setBasePath("/api/auth/refresh")
		  .setPort(TestConstants.SERVER_PORT)
		  .addFilter(new RequestLoggingFilter(LogDetail.ALL))
		  .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		  .build();

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("email", "any@sb.com")
		  .when()
		  .put("{email}")
		  .then()
		  .statusCode(404)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Resource with id (any@sb.com) not exists."));
	}

	@Test
	@Order(9)
	@DisplayName("shouldn't refresh token without token")
	void testRefreshTokenWithoutToken() {

		given()
		  .basePath("/api/auth/refresh")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("email", "admin@sb.com")
		  .when()
		  .put("{email}")
		  .then()
		  .statusCode(400);
	}

	private RequestSpecification loginAdmin() {
		CredentialsDto credentials = new CredentialsDto("admin@sb.com", "12345678");

		var token = given()
		  .basePath("/api/auth/signin")
		  .port(TestConstants.SERVER_PORT)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .body(credentials)
		  .when()
		  .post()
		  .then()
		  .statusCode(200)
		  .extract().body().as(AuthDto.class).token();

		return new RequestSpecBuilder()
		  .addHeader("Authorization", "Bearer " + token.accessToken())
		  .setBasePath("/api/auth/manager/signup")
		  .setPort(TestConstants.SERVER_PORT)
		  .addFilter(new RequestLoggingFilter(LogDetail.ALL))
		  .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		  .build();
	}
}
