package com.sb.products.services;

import com.sb.products.exceptions.ResourceRequiredException;
import com.sb.products.mocks.MockProduct;
import com.sb.products.models.Product;
import com.sb.products.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	MockProduct mock;

	@InjectMocks
	private ProductService service;

	@Mock
	private ProductRepository repository;

	@BeforeEach
	public void setup() {
		mock = new MockProduct();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testCreateProduct() throws Exception {
		Product product = mock.createEntity();

		when(repository.save(any())).thenReturn(product);

		var result = service.create(mock.createDto());

		assertNotNull(result);
		assertEquals(result.getName(), product.getName());
		assertEquals(result.getDescription(), product.getDescription());
		assertEquals(result.getPrice(), product.getPrice());
	}

	@Test
	public void testCreateWithNullProduct() {
		Exception exception = assertThrows(ResourceRequiredException.class, () -> {
			service.create(null);
		});

		String expectedMessage = "Resource cannot be null.";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}
}
