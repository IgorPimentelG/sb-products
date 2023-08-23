package com.sb.products.data.gateway;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductGateway {
	Product create(Product product) throws RequiredException;
	Product update(String id, Product product) throws RequiredException, NotFoundException;
	Product findById(String id) throws NotFoundException;
	Page<Product> findAll(Pageable pageable, String name);
	void delete(String id) throws NotFoundException;
}
