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
	void shouldRegisterCommonUser() {

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

	//

	@Test
	@Order(1)
	void shouldNotRegisterCommonUserWithDuplicateEmail() {

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
	void shouldNotRegisterCommonUserWithInvalidFields() {

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
	void shouldRegisterManagerUser() {
		CredentialsDto credentials = new CredentialsDto("admin@sb.com", "12345678");
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
	void shouldNotRegisterManagerUserWithDuplicateEmail() {

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
	void shouldNotRegisterManagerUserWithInvalidFields() {

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
	void shouldNotRegisterManagerUserWithoutIsAuthenticatedAsAdmin() {

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
