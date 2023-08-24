package com.sb.products.mocks;

import com.sb.products.domain.entities.Product;
import com.sb.products.infra.controller.dtos.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockProduct {

	public Product createEntity() {
		return createEntity(0);
	}

	public ProductDto createDto() {
		return new ProductDto(
		  "Any Name",
		  "Any Description",
		  2000,
		  "594958"
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
		  "id " + number,
		  "Any Name " + number,
		  "Any Description " + number,
		  BigDecimal.valueOf(2.500),
		  "9658586"
		);
	}
}
