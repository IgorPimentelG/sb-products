package com.sb.products.controllers;

import com.sb.products.controllers.docs.product.*;
import com.sb.products.exceptions.ResourceNotFoundException;
import com.sb.products.exceptions.ResourceRequiredException;
import com.sb.products.models.Product;
import com.sb.products.models.dto.product.ProductDTO;
import com.sb.products.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/api/product")
@Tag(name = "Product", description = "Endpoints for managing products")
public class ProductController {

	@Autowired
	private ProductService service;

	@CreateDoc
	@PostMapping(value = "/v1")
	public ResponseEntity<Product> create(@RequestBody @Valid ProductDTO product)
	  throws ResourceRequiredException {
		var body = service.create(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@UpdateDoc
	@PutMapping(value = "/v1/{id}")
	public ResponseEntity<Product> update(
	  @PathVariable("id") String id,
	  @RequestBody @Valid ProductDTO product) throws ResourceNotFoundException {
		var entity = service.update(id, product);
		entity.add(
		  linkTo(
			methodOn(ProductController.class).findById(id)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}

	@FindByIdDoc
	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<Product> findById(@PathVariable("id") String id)
	  throws ResourceNotFoundException {
		var entity = service.findById(id);
		entity.add(
		  linkTo(
			methodOn(ProductController.class).findAll()).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entity);
	}

	@FindByIdDoc
	@GetMapping(value = "/v2")
	public ResponseEntity<Page<Product>> findAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<Product> products = service.findAll(pageable);
		products.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(ProductController.class).findById(item.getId())).withSelfRel()
				);
			} catch (Exception ignored) {}
		});

		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@FindAllDoc
	@GetMapping(value = "/v1")
	public ResponseEntity<List<Product>> findAll() {
		List<Product> products = service.findAll();
		products.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(ProductController.class).findById(item.getId())).withSelfRel()
				);
			} catch (Exception ignored) {}
		});

		return ResponseEntity.status(HttpStatus.OK).body(products);
	}

	@DeleteDoc
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id)
	  throws ResourceNotFoundException {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
