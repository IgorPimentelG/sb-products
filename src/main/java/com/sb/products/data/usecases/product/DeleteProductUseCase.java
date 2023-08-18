package com.sb.products.data.usecases.product;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.gateway.ProductGateway;

public class DeleteProductUseCase {

	private final ProductGateway productGateway;

	public DeleteProductUseCase(ProductGateway productGateway) {
		this.productGateway = productGateway;
	}

	public void execute(Input input) throws NotFoundException {
		productGateway.delete(input.id());
	}

	public record Input(String id) {}
}
