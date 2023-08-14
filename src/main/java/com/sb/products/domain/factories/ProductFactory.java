package com.sb.products.domain.factories;

import com.sb.products.domain.entities.Product;

import java.math.BigDecimal;

public abstract class ProductFactory {
	public static Product create(String name, String description, double price, String barcode) {
		return new Product(
		  name,
		  description,
		  BigDecimal.valueOf(price),
		  barcode
		);
	}

	public static Product create(String name, double price, String barcode) {
		return new Product(
		  name,
		  BigDecimal.valueOf(price),
		  barcode
		);
	}
}
