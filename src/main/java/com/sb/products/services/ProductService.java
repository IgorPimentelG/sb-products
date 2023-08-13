package com.sb.products.services;

import com.sb.products.exceptions.ResourceNotFoundException;
import com.sb.products.exceptions.ResourceRequiredException;
import com.sb.products.model.Product;
import com.sb.products.model.dto.product.ProductDTO;
import com.sb.products.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;

	private final Logger logger = Logger.getLogger(ProductService.class.getName());

	public Product create(ProductDTO product) throws ResourceRequiredException {
		if (product == null) {
			logger.log(Level.WARNING, "Product cannot be null.");
			throw new ResourceRequiredException();
		}

		var entity = new Product();
		BeanUtils.copyProperties(product, entity);
		var createdProduct = repository.save(entity);

		logger.log(Level.INFO, "Product created.");

		return createdProduct;
	}

	public Product update(String id, ProductDTO product) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		BeanUtils.copyProperties(product, entity);
		repository.save(entity);

		logger.log(Level.INFO, "Product updated.");

		return entity;
	}

	public Product findById(String id) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		logger.log(Level.INFO, "Find product.");

		return entity;
	}

	public List<Product> findAll() {
		logger.log(Level.INFO, "Find all products.");
		return repository.findAll();
	}

	public void delete(String id) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		repository.delete(entity);
		logger.log(Level.INFO, "Product deleted.");
	}
}
