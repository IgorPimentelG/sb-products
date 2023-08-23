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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
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
	private PagedResourcesAssembler<ProductSchema> assembler;

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
	  @RequestBody @Valid ProductDto product
	) throws NotFoundException, RequiredException {
		var entity = gateway.update(id, mapper.toEntity(product));
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(ProductController.class).findById(id)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@FindByIdDoc
	@GetMapping(value = "/v1/{id}")
	public ResponseEntity<ProductSchema> findById(@PathVariable("id") String id)
	  throws NotFoundException {
		var entity = gateway.findById(id);
		var entitySchema = mapper.toSchema(entity);
		entitySchema.add(
		  linkTo(
			methodOn(ProductController.class).findAll(0, 10, "asc", "*")).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(entitySchema);
	}

	@FindAllDoc
	@GetMapping(value = "/v1")
	public ResponseEntity<PagedModel<EntityModel<ProductSchema>>> findAll(
	  @RequestParam(value = "page", defaultValue = "0") int page,
	  @RequestParam(value = "limit", defaultValue = "10") int limit,
	  @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy,
	  @RequestParam(value = "name", defaultValue = "*") String name
	) {
		var sortDirection = "desc".equalsIgnoreCase(orderBy) ? Direction.DESC : Direction.ASC;

		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));
		Page<Product> products = gateway.findAll(pageable, name);

		var listSchema = products.map(mapper::toSchema);
		listSchema.forEach(item -> {
			try {
				item.add(
				  linkTo(
					methodOn(ProductController.class).findById(item.getId())).withSelfRel()
				);
			} catch (Exception ignored) {}
		});

		var pagedModel = assembler.toModel(
		  listSchema,
		  linkTo(methodOn(ProductController.class).findAll(page, limit, orderBy, name)).withSelfRel()
		);

		return ResponseEntity.status(HttpStatus.OK).body(pagedModel);
	}

	@DeleteDoc
	@DeleteMapping(value = "/v1/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id)
	  throws NotFoundException {
		gateway.delete(id);
		return ResponseEntity.noContent().build();
	}
}
