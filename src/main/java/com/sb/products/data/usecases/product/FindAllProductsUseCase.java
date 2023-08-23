package com.sb.products.data.usecases.product;

import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class FindAllProductsUseCase {

	private final ProductGateway productGateway;

	public FindAllProductsUseCase(ProductGateway productGateway) {
		this.productGateway = productGateway;
	}

	public Output execute(Input input) {
		var products = productGateway.findAll(input.pageable(), input.name());

		return new Output(products);
	}

	public record Input(Pageable pageable, String name) {}

	public record Output(Page<Product> products) {}
}
