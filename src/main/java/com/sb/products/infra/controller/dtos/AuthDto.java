package com.sb.products.infra.controller.dtos;

import com.sb.products.data.gateway.outputs.TokenOutput;

public record AuthDto(UserDto user, TokenOutput token) {}
