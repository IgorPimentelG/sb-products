package com.sb.products.data.gateway.outputs;

import java.util.Date;

public record TokenOutput(
  String accessToken,
  String refreshToken,
  Date createdAt,
  Date expirationAccessToken,
  Date expirationRefreshToken
) {}
