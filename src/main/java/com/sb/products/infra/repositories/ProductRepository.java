package com.sb.products.infra.repositories;

import com.sb.products.domain.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
	@Query("SELECT product FROM Product product WHERE product.name LIKE LOWER(CONCAT('%',:name,'%'))")
	Page<Product> findByName(@Param("name") String name, Pageable pageable);
}
