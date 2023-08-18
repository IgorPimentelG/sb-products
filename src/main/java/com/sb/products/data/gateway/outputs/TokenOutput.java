package com.sb.products.data.gateway.outputs;

import java.util.Date;

public record TokenOutput(
  String accessToken,
  Date createdAt,
  Date expirationAt
) {}
