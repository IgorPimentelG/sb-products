package com.sb.products.infra.mapper;

import com.sb.products.domain.entities.Product;
import com.sb.products.infra.controller.dtos.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductMapper {
	ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

	@Mapping(target = "id", ignore = true)
	Product toEntity(ProductDto dto);
}
