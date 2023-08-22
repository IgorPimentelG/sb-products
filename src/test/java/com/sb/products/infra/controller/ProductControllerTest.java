package com.sb.products.infra.controller;

import com.sb.products.domain.entities.Product;
import com.sb.products.infra.AbstractIntegration;
import com.sb.products.infra.TestConstants;
import com.sb.products.infra.controller.dtos.AuthDto;
import com.sb.products.infra.controller.dtos.CredentialsDto;
import com.sb.products.infra.controller.dtos.ProductDto;
import com.sb.products.main.Application;
import com.sb.products.main.config.handles.ExceptionResponse;
import com.sb.products.mocks.MockProduct;
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
public class ProductControllerTest extends AbstractIntegration {

	static Product product;
	static MockProduct mock;
	static RequestSpecification specification;

	@BeforeAll
	static void setup() {
		mock = new MockProduct();
	}

	@Test
	@Order(0)
	@DisplayName("should authenticate user")
	void authentication() {
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

		specification = new RequestSpecBuilder()
		  .addHeader("Authorization", "Bearer " + token.accessToken())
		  .setBasePath("/api/product/v1")
		  .setPort(TestConstants.SERVER_PORT)
		  .addFilter(new RequestLoggingFilter(LogDetail.ALL))
		  .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
		  .build();
	}

	@Test
	@Order(1)
	@DisplayName("should create a new product")
	void testCreateProduct() {
		var productDto = mock.createDto();

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .body(productDto)
		  .when()
		  .post()
		  .then()
		  .statusCode(201)
		  .extract().body().as(Product.class);

		product = result;

		assertNotNull(result);
	}

	@Test
	@Order(2)
	@DisplayName("shouldn't create a new product with invalid fields")
	void testCreateProductWithInvalidFields() {

		var productDto = new ProductDto(
		  "",
		  "",
		  0,
		  ""
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .body(productDto)
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
	@DisplayName("should find a product")
	void testFindProduct() {
		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", product.getId())
		  .when()
		  .get("{id}")
		  .then()
		  .statusCode(200)
		  .extract().body();

		assertNotNull(result);
	}

	@Test
	@Order(4)
	@DisplayName("shouldn't find a product when it doesn't exists")
	void testFindProductThatDoesNotExists() {
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
	@Order(5)
	@DisplayName("should list all products")
	void testListAllProducts() {
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
	@Order(6)
	@DisplayName("should update a product")
	void testUpdateProduct() {

		var productDto = new ProductDto(
			"Updated Name",
		  "Updated Description",
		  2000.00,
		  "68375758"
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", product.getId())
		  .body(productDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(200)
		  .extract().body();

		assertNotNull(result);
	}

	@Test
	@Order(7)
	@DisplayName("shouldn't update a product when it doesn't exists")
	void testUpdateProductThatDoesNotExists() {
		var productDto = new ProductDto(
		  "Updated Name",
		  "Updated Description",
		  2000.00,
		  "68375758"
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", "non-existent-id")
		  .body(productDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(404)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Resource with id (non-existent-id) not exists."));
	}

	@Test
	@Order(8)
	@DisplayName("shouldn't create a new product with invalid fields")
	void testUpdateAProductWithInvalidFields() {
		var productDto = new ProductDto(
		  "",
		  "",
		  0,
		  ""
		);

		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", product.getId())
		  .body(productDto)
		  .when()
		  .put("{id}")
		  .then()
		  .statusCode(400)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Invalid field"));
	}

	@Test
	@Order(9)
	@DisplayName("should delete a product")
	void testDeleteProduct() {
		given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", product.getId())
		  .when()
		  .delete("{id}")
		  .then()
		  .statusCode(204);
	}

	@Test
	@Order(10)
	@DisplayName("shouldn't delete a product when it doesn't exists")
	void testDeleteProductThatDoesNotExists() {
		var result = given()
		  .spec(specification)
		  .contentType(TestConstants.CONTENT_TYPE_JSON)
		  .header("Origin", TestConstants.ORIGIN_LOCALHOST)
		  .pathParam("id", "non-existent-id")
		  .when()
		  .delete("{id}")
		  .then()
		  .statusCode(404)
		  .extract().body().as(ExceptionResponse.class);

		assertNotNull(result);
		assertTrue(result.message().contains("Resource with id (non-existent-id) not exists."));
	}
}