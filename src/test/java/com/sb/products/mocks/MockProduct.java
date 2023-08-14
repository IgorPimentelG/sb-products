package com.sb.products.mocks;

import com.sb.products.models.Product;
import com.sb.products.models.dto.product.ProductDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockProduct {

	public Product createEntity() {
		return createEntity(0);
	}

	public ProductDTO createDto() {
		return new ProductDTO(
		  "Any Name",
		  "Any Description",
		  BigDecimal.valueOf(2.500)
		);
	}

	public List<Product> createListEntity() {
		List<Product> products = new ArrayList<>();

		for (int i = 0; i < 20; i++) {
			products.add(createEntity(i));
		}

		return products;
	}

	public Product createEntity(int number) {
		return new Product(
		  "Any Name " + number,
		  "Any Description " + number,
		  BigDecimal.valueOf(2.500)
		);
	}
}
