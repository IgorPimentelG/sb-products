package com.sb.products.data.gateway.outputs;

import com.sb.products.domain.entities.User;

public record AuthOutput(
  User user,
  TokenOutput token
) {}
