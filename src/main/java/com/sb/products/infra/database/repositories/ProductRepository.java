package com.sb.products.infra.database.repositories;

import com.sb.products.infra.database.schemas.ProductSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductSchema, String> {
	@Query("SELECT product FROM ProductSchema product WHERE product.name LIKE LOWER(CONCAT('%',:name,'%'))")
	Page<ProductSchema> findByName(@Param("name") String name, Pageable pageable);
}
