package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.domain.entities.Product;
import com.sb.products.infra.database.repositories.ProductRepository;
import com.sb.products.infra.database.schemas.ProductSchema;
import com.sb.products.mocks.MockProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

public class ProductGatewayTest {
	MockProduct mock;

	@InjectMocks
	private ProductService gateway;

	@Mock
	private ProductRepository repository;

	@BeforeEach
	public void setup() {
		mock = new MockProduct();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("should create a product")
	public void testCreateProduct() throws Exception {
		ProductSchema product = mock.createEntitySchema();

		when(repository.save(any())).thenReturn(product);

		var result = gateway.create(mock.createEntity());

		verify(repository, times(1)).save(any());

		assertNotNull(result);
		assertEquals(result.getName(), product.getName());
		assertEquals(result.getDescription(), product.getDescription());
		assertEquals(result.getPrice(), product.getPrice());
	}

	@Test
	@DisplayName("should throws RequiredException when create a product with null data")
	public void testThrowsRequiredExceptionWhenCreateWithNullProduct() {
		Exception exception = assertThrows(RequiredException.class, () -> {
			gateway.create(null);
		});

		String expectedMessage = "Resource cannot be null.";
		String resultMessage = exception.getMessage();

		verify(repository, times(0)).save(any());
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	@DisplayName("should update a product")
	public void testUpdateProduct() throws Exception {
		Product product = mock.createEntity();
		ProductSchema productSchema =  mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(productSchema));

		var result = gateway.update("Any Identifier", product);

		verify(repository, times(1)).findById(any());
		verify(repository, times(1)).save(any());
		assertNotNull(result);
		assertEquals(result.getName(), productSchema.getName());
		assertEquals(result.getDescription(), productSchema.getDescription());
		assertEquals(result.getPrice(), productSchema.getPrice());
	}

	@Test
	@DisplayName("should throws NotFoundException when update a product that does not exists")
	public void testThrowsNotFoundExceptionWhenUpdateNonExistentProduct() {
		Product product =  mock.createEntity();

		when(repository.findById(any())).thenReturn(Optional.empty());

		Exception exception = assertThrows(NotFoundException.class, () -> {
			gateway.update("Any Identifier", product);
		});

		String expectedMessage = "Resource with id (Any Identifier) not exists.";
		String resultMessage = exception.getMessage();

		verify(repository, times(1)).findById(any());
		verify(repository, times(0)).save(any());
		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	@DisplayName("should throws NotFoundException when update a product that does not exists")
	public void testThrowsRequiredExceptionWhenUpdateWithNullProduct() {
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
	@DisplayName("should find a product")
	public void testFindProduct() throws Exception {
		ProductSchema product = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(product));

		var result = gateway.findById("Any Identifier");

		verify(repository, times(1)).findById(any());
		assertNotNull(result);
		assertEquals(product.getName(), result.getName());
		assertEquals(product.getDescription(), result.getDescription());
		assertEquals(product.getPrice(), result.getPrice());
	}

	@Test
	@DisplayName("should throws NotFoundException when find a product that does not exists")
	public void testThrowsNotFoundExceptionWhenFindNonExistentProduct() {
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
	@DisplayName("should find all products")
	public void testFindAllProducts() {
		Page<ProductSchema> products = new PageImpl<>(mock.createListEntitySchema());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(products);

		var result = gateway.findAll(pageable, "*");

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(products.getContent().size(), result.getContent().size());
	}

	@Test
	@DisplayName("should find all products filtered by name")
	public void testFindAllProductsFilteredByName() {
		Page<ProductSchema> products = new PageImpl<>(mock.createListEntitySchema());
		Pageable pageable = PageRequest.of(0, 1);
		var productName = products.getContent().get(0).getName();

		when(repository.findByName(productName, pageable)).thenReturn(products);

		var result = gateway.findAll(pageable, productName);

		verify(repository, times(1)).findByName(productName, pageable);
		assertNotNull(result);
		assertEquals(products.getContent().size(), result.getContent().size());
	}

	@Test
	@DisplayName("should find all product when does not exist products")
	public void testFindAllWithEmptyProducts() {
		Page<ProductSchema> products = new PageImpl<>(new ArrayList<>());
		Pageable pageable = PageRequest.of(0, 1);

		when(repository.findAll(pageable)).thenReturn(products);

		var result = gateway.findAll(pageable, "*");

		verify(repository, times(1)).findAll(pageable);
		assertNotNull(result);
		assertEquals(result.getContent().size(), 0);
	}

	@Test
	@DisplayName("should delete a product")
	public void testDeleteProduct() throws Exception {
		ProductSchema product = mock.createEntitySchema();

		when(repository.findById(any())).thenReturn(Optional.of(product));

		gateway.delete("Any Identifier");

		verify(repository, times(1)).delete(any());
	}

	@Test
	@DisplayName("should throws NotFoundException when delete a product that does not exists")
	public void testThrowsNotFoundExceptionWhenDeleteNonExistentProduct() {
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
