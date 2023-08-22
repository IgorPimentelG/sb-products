package com.sb.products.infra.controller.dtos;

import java.util.List;

public record UserDto(
  String id,
  String fullName,
  String email,
  List<String> permissions,
  boolean enabled,
  boolean accountNonExpired,
  boolean accountNonLocked,
  boolean credentialsNonExpired
) {}
