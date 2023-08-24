package com.sb.products.infra.mapper;

import com.sb.products.domain.entities.User;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserRegisterDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	@Mapping(target = "id", ignore = true)
	User toEntity(UserRegisterDto dto);

	@Mapping(target = "permissions", ignore = true)
	UserDto toDto(User user);
}
