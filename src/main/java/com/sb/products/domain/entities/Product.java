package com.sb.products.domain.entities;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product extends RepresentationModel<Product> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String barcode;
	private String name;

	@Column(nullable = true)
	private String description;

	private BigDecimal price;

	public Product() {}

	public Product(String id, String name, String description, BigDecimal price, String barcode) {
		setId(id);
		setName(name);
		setPrice(price);
		setBarcode(barcode);
		setDescription(description);
	}

	public Product(String id, String name, BigDecimal price, String barcode) {
		setId(id);
		setName(name);
		setPrice(price);
		setBarcode(barcode);
	}

	public void setId(String id) {
		this.id = id;
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

	public String getId() { return id; }

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
