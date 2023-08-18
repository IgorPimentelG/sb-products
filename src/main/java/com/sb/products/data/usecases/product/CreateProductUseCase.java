package com.sb.products.data.usecases.product;

import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.domain.entities.Product;
import com.sb.products.domain.factories.ProductFactory;

public class CreateProductUseCase {

	private final ProductGateway productGateway;

	public CreateProductUseCase(ProductGateway productGateway) {
		this.productGateway = productGateway;
	}

	public Output execute(Input input) throws RequiredException {
		var product = ProductFactory.create(
		  input.id(),
		  input.name(),
		  input.description(),
		  input.price(),
		  input.barcode()
		);

		var result = productGateway.create(product);

		return new Output(result);
	}

	public record Input(String id, String name, String description, double price, String barcode) {}

	public record Output(Product product) {}
}
