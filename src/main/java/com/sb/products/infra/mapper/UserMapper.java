package com.sb.products.infra.mapper;

import com.sb.products.domain.entities.User;
import com.sb.products.infra.controller.dtos.UserDto;
import com.sb.products.infra.controller.dtos.UserRegisterDto;
import com.sb.products.infra.controller.dtos.UserUpdateDto;
import com.sb.products.infra.database.schemas.UserSchema;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	User toEntity(UserSchema schema);

	@Mapping(target = "id", ignore = true)
	User toEntity(UserRegisterDto dto);


	@Mapping(target = "id", ignore = true)
	User toEntity(UserUpdateDto dto);

	@Mapping(target = "permissions", ignore = true)
	UserDto toDto(User user);

	UserSchema toSchema(User user);

	@IterableMapping(elementTargetType = User.class)
	List<User> toListEntity(List<UserSchema> schemas);

	@IterableMapping(elementTargetType = UserSchema.class)
	List<UserSchema> toListSchema(List<User> users);
}
