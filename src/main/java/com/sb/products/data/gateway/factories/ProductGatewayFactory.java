package com.sb.products.data.gateway.factories;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.data.usecases.product.*;
import com.sb.products.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public abstract class ProductGatewayFactory {

	public static ProductGateway create(ProductGateway productGateway) {
		return new Output(
		  new CreateProductUseCase(productGateway),
		  new UpdateProductUseCase(productGateway),
		  new FindProductByIdUseCase(productGateway),
		  new FindAllProductsUseCase(productGateway),
		  new DeleteProductUseCase(productGateway)
		);
	}

	public record Output(
	  CreateProductUseCase createUseCase,
	  UpdateProductUseCase updateUseCase,
	  FindProductByIdUseCase findUseCase,
	  FindAllProductsUseCase findAllUseCase,
	  DeleteProductUseCase deleteUseCase
	) implements ProductGateway {
		@Override
		public Product create(Product product) throws RequiredException {
			return createUseCase.execute(new CreateProductUseCase.Input(
			  product.getId(),
			  product.getName(),
			  product.getDescription(),
			  product.getPrice().doubleValue(),
			  product.getBarcode()
			)).product();
		}

		@Override
		public Product update(String id, Product product) throws RequiredException, NotFoundException {
			return updateUseCase.execute(new UpdateProductUseCase.Input(
			  id,
			  product.getName(),
			  product.getDescription(),
			  product.getPrice().doubleValue(),
			  product.getBarcode()
			)).product();
		}

		@Override
		public Product findById(String id) throws NotFoundException {
			return findUseCase.execute(new FindProductByIdUseCase.Input(id))
			  .product();
		}

		@Override
		public Page<Product> findAll(Pageable pageable, String name) {
			return findAllUseCase.execute(new FindAllProductsUseCase.Input(pageable, name))
			  .products();
		}

		@Override
		public void delete(String id) throws NotFoundException {
			deleteUseCase.execute(new DeleteProductUseCase.Input(id));
		}
	}
}
