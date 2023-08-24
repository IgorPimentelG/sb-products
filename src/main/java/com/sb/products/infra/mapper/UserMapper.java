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
	@Mapping(target = "accountNonExpired", ignore = true)
	@Mapping(target = "accountNonLocked", ignore = true)
	@Mapping(target = "credentialsNonExpired", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(target = "roles", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	@Mapping(target = "authorities", ignore = true)
	User toEntity(UserRegisterDto dto);

	@Mapping(target = "permissions", ignore = true)
	UserDto toDto(User user);
}
