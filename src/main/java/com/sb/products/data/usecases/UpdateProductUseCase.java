package com.sb.products.data.usecases;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.domain.entities.Product;
import com.sb.products.domain.factories.ProductFactory;

public class UpdateProductUseCase {

	private final ProductGateway productGateway;

	public UpdateProductUseCase(ProductGateway productGateway) {
		this.productGateway = productGateway;
	}

	public Output execute(Input input) throws RequiredException, NotFoundException {
		var product = ProductFactory.create(
		  input.id(), input.name(), input.description(), input.price(), input.barcode()
		);

		var result = productGateway.update(input.id(), product);

		return new Output(product);
	}

	public record Input(String id, String name, String description, double price, String barcode) {}

	public record Output(Product product) {}
}
