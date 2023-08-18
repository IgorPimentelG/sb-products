package com.sb.products.infra.controller;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.gateway.ProductGateway;
import com.sb.products.data.gateway.factories.ProductGatewayFactory;
import com.sb.products.domain.entities.Product;
import com.sb.products.infra.controller.docs.product.*;
import com.sb.products.infra.controller.dtos.ProductDto;
import com.sb.products.infra.database.schemas.ProductSchema;
import com.sb.products.infra.mapper.ProductMapper;
import com.sb.products.infra.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product", description = "Endpoints for managing products")
public class ProductController {

	private final ProductGateway gateway;
	private final ProductMapper mapper = ProductMapper.INSTANCE;

	@Autowired
	public ProductController(ProductService databaseGateway) {
		this.gateway = ProductGatewayFactory.create(databaseGateway);
	}

	@CreateDoc
	@PostMapping(value = "/v1")
	public ResponseEntity<Product> create(@RequestBody @Valid ProductDto product)
	  throws RequiredException {
		var body = gateway.create(mapper.toEntity(product));
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@UpdateDoc
	@PutMapping(value = "/v1/{id}")
	public ResponseEntity<ProductSchema> update(
	  @PathVariable("id") String id,
	  @RequestBody @Valid ProductDto product,
	  Pageable pageable) throws NotFoundException, RequiredException {
		var entity = gateway.update(id, mapper.toEntity(product));
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(ProductController.class).findById(id, pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@FindByIdDoc
	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<ProductSchema> findById(@PathVariable("id") String id, Pageable pageable)
	  throws NotFoundException {
		var entity = gateway.findById(id);
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(ProductController.class).findAll(pageable)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@FindAllDoc
	@GetMapping(value = "/v1")
	public ResponseEntity<Page<ProductSchema>> findAll(@PageableDefault(size = 5) Pageable pageable) {
		Page<Product> products = gateway.findAll(pageable);
		var listSchema = mapper.toListSchema(products.stream().toList());
		listSchema.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(ProductController.class).findById(item.getId(), pageable)).withSelfRel()
				);
			} catch (Exception ignored) {}
		});

		return ResponseEntity.status(HttpStatus.OK).body(new PageImpl<>(listSchema));
	}

	@DeleteDoc
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id)
	  throws NotFoundException {
		gateway.delete(id);
		return ResponseEntity.noContent().build();
	}
}
