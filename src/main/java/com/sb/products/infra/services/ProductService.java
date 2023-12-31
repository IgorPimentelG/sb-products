package com.sb.products.infra.services;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.domain.entities.Product;
import com.sb.products.infra.mapper.ProductMapper;
import com.sb.products.infra.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService implements ProductGateway {

	@Autowired
	private ProductRepository repository;

	private final ProductMapper mapper = ProductMapper.INSTANCE;
	private final Logger logger = Logger.getLogger(ProductService.class.getName());

	@Override
	public Product create(Product product) throws RequiredException {
		if (product == null) {
			logger.log(Level.WARNING, "[V1] Product cannot be null.");
			throw new RequiredException();
		}

		var entity = repository.save(product);

		logger.log(Level.INFO, "[V1] Product created.");

		return entity;
	}

	@Override
	public Product update(String id, Product product) throws RequiredException, NotFoundException {
		if (product == null) {
			logger.log(Level.WARNING, "[V1] Product cannot be null.");
			throw new RequiredException();
		}

		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new NotFoundException(id);
		  });

		BeanUtils.copyProperties(product, entity);
		repository.save(entity);

		logger.log(Level.INFO, "[V1] Product updated.");

		return entity;
	}

	@Override
	public Product findById(String id) throws NotFoundException {
		var product = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new NotFoundException(id);
		  });

		logger.log(Level.INFO, "[V1] Find product.");

		return product;
	}

	@Override
	public Page<Product> findAll(Pageable pageable, String name) {
		logger.log(Level.INFO, "[V1] Find all products.");

		Page<Product> products;

		if (name.equals("*") || name.isEmpty()) {
			products = repository.findAll(pageable);
		} else {
			products = repository.findByName(name, pageable);
		}

		return products;
	}

	@Override
	public void delete(String id) throws NotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new NotFoundException(id);
		  });

		repository.delete(entity);
		logger.log(Level.INFO, "[V1] Product deleted.");
	}
}
