package com.sb.products.infra.database.repositories;

import com.sb.products.infra.database.schemas.ProductSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, String> {}
