package com.sb.products.infra.controller.dtos;

import org.hibernate.validator.constraints.Length;

public record UserUpdateDto(
  @Length(min = 5, max = 100, message = "Full name must be at least 5 to a max of 100 characters")
  String fullName,

  @Length(min = 8, max = 30, message = "Password must be at least 8 to a max of 30 characters")
  String password,

  boolean enabled,
  boolean accountNonExpired,
  boolean accountNonLocked,
  boolean credentialsNonExpired
) {}