package com.sb.products.data.usecases.product;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.domain.entities.Product;

public class FindProductByIdUseCase {

	private final ProductGateway productGateway;

	public FindProductByIdUseCase(ProductGateway productGateway) {
		this.productGateway = productGateway;
	}

	public Output execute(Input input) throws NotFoundException {
		var product = productGateway.findById(input.id());

		return new Output(product);
	}

	public record Input(String id) {}

	public record Output(Product product) {}
}
