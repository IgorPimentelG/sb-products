package com.sb.products.domain.entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
	private String barcode;
	private String name;
	private String description;
	private BigDecimal price;

	public Product(String name, String description, BigDecimal price, String barcode) {
		setName(name);
		setPrice(price);
		setBarcode(barcode);
		setDescription(description);
	}

	public Product(String name, BigDecimal price, String barcode) {
		setName(name);
		setPrice(price);
		setBarcode(barcode);
	}

	public void setBarcode(String barcode) throws IllegalArgumentException {
		if (this.barcode == null) {
			if(!barcode.matches("^[0-9]*$")) {
				throw new IllegalArgumentException("Barcode can only be number");
			} else if (barcode.isEmpty() || barcode.isBlank()) {
				throw new IllegalArgumentException("Barcode cannot be empty");
			}

			this.barcode = barcode;
		}
	}

	public void setName(String name) throws IllegalArgumentException {
		if (name.isBlank() || name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPrice(BigDecimal price) throws IllegalArgumentException {
		if (price.compareTo(BigDecimal.valueOf(0)) <= 0) {
			throw new IllegalArgumentException("Price cannot be less than zero");
		}
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getBarcode() {
		return barcode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Product product = (Product) o;
		return Objects.equals(barcode, product.barcode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(barcode);
	}
}
