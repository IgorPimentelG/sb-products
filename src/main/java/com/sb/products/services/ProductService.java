package com.sb.products.services;

import com.sb.products.exceptions.ResourceNotFoundException;
import com.sb.products.exceptions.ResourceRequiredException;
import com.sb.products.model.Product;
import com.sb.products.model.dto.product.ProductDTO;
import com.sb.products.repositories.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
			logger.log(Level.WARNING, "[V1] Product cannot be null.");
			throw new ResourceRequiredException();
		}

		var entity = new Product();
		BeanUtils.copyProperties(product, entity);
		var createdProduct = repository.save(entity);

		logger.log(Level.INFO, "[V1] Product created.");

		return createdProduct;
	}

	public Product update(String id, ProductDTO product) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		BeanUtils.copyProperties(product, entity);
		repository.save(entity);

		logger.log(Level.INFO, "[V1] Product updated.");

		return entity;
	}

	public Product findById(String id) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		logger.log(Level.INFO, "[V1] Find product.");

		return entity;
	}

	public List<Product> findAll() {
		logger.log(Level.INFO, "[V1] Find all products.");
		return repository.findAll();
	}

	public Page<Product> findAll(Pageable pageable) {
		logger.log(Level.INFO, "[V2] Find all products.");
		return repository.findAll(pageable);
	}

	public void delete(String id) throws ResourceNotFoundException {
		var entity = repository.findById(id).orElseThrow(
		  () -> {
			  logger.log(Level.WARNING, "[V1] Product not found.");
			  return new ResourceNotFoundException(id);
		  });

		repository.delete(entity);
		logger.log(Level.INFO, "[V1] Product deleted.");
	}
}
