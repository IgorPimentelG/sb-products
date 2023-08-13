package com.sb.products.controller;

import com.sb.products.exceptions.ResourceNotFoundException;
import com.sb.products.exceptions.ResourceRequiredException;
import com.sb.products.model.Product;
import com.sb.products.model.dto.product.ProductDTO;
import com.sb.products.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

	@Autowired
	private ProductService service;

	@PostMapping(value = "/v1")
	public ResponseEntity<Product> create(@RequestBody @Valid ProductDTO product)
	  throws ResourceRequiredException {
		var body = service.create(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@PutMapping(value = "/v1/{id}")
	public ResponseEntity<Product> update(
	  @PathVariable("id") String id,
	  @RequestBody @Valid ProductDTO product) throws ResourceNotFoundException {
		var body = service.update(id, product);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<Product> findById(@PathVariable("id") String id)
	  throws ResourceNotFoundException {
		var body = service.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}

	@GetMapping(value = "/v1")
	public ResponseEntity<List<Product>> findAll() {
		List<Product> products = service.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id)
	  throws ResourceNotFoundException {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
