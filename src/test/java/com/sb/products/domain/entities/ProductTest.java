package com.sb.products.domain.entities;

import com.sb.products.domain.factories.ProductFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

	@Test
	void shouldCreateProduct() {
		var id = "any id";
		var name = "any name";
		var description = "any description";
		var barcode = "594869584";
		var price = 2.500D;

		var product = ProductFactory.create(id, name, description, price, barcode);

		assertNotNull(product);
		assertEquals(product.getId(), id);
		assertEquals(product.getName(), name);
		assertEquals(product.getDescription(), description);
		assertEquals(product.getPrice(), BigDecimal.valueOf(price));
		assertEquals(product.getBarcode(), barcode);
	}

	@Test
	void shouldCreateProductWithoutDescription() {
		var id = "any id";
		var name = "any name";
		var barcode = "594869584";
		var price = 2.500D;

		var product = ProductFactory.create(id, name, price, barcode);

		assertNotNull(product);
		assertEquals(product.getId(), id);
		assertEquals(product.getName(), name);
		assertEquals(product.getPrice(), BigDecimal.valueOf(price));
		assertEquals(product.getBarcode(), barcode);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenBarcodeIsEmpty() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			ProductFactory.create("any id", "any name", 2.500D, "");
		});

		String expectedMessage = "Barcode cannot be empty";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenBarcodeHasCharacterNotNumber() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			ProductFactory.create("any id", "any name", 2.500D, "abc");
		});

		String expectedMessage = "Barcode can only be number";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenNameIsEmpty() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			ProductFactory.create("any id", "", 2.500D, "53647664");
		});

		String expectedMessage = "Name cannot be empty";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}

	@Test
	void shouldThrowsIllegalArgumentExceptionWhenPriceIsLessThanZero() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			ProductFactory.create("any id", "any name", 0, "53647664");
		});

		String expectedMessage = "Price cannot be less than zero";
		String resultMessage = exception.getMessage();

		assertEquals(expectedMessage, resultMessage);
	}
}
