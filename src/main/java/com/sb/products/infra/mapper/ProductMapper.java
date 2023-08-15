package com.sb.products.infra.mapper;

import com.sb.products.domain.entities.Product;
import com.sb.products.infra.controller.dtos.ProductDto;
import com.sb.products.infra.database.schemas.ProductSchema;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
	ProductMapper INSTANCE = Mappers.getMapper((ProductMapper.class));

	Product toEntity(ProductSchema schema);
	Product toEntity(ProductDto dto);

	ProductSchema toSchema(Product product);

	@IterableMapping(elementTargetType = Product.class)
	List<Product> toListEntity(List<ProductSchema> schemas);

	@IterableMapping(elementTargetType = ProductSchema.class)
	List<ProductSchema> toListSchema(List<Product> schemas);
}
