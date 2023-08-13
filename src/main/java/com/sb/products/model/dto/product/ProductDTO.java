package com.sb.products.model.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record ProductDTO(
  @NotBlank(message = "Invalid Name: empty name")
  @Length(min = 3, max = 255, message = "Invalid name: must be of 3 - 255 characters")
  String name,

  @Length(max = 500, message = "Invalid Description: must be a max of 500 characters")
  String description,

  @NotNull(message = "Invalid Price: empty price")
  @DecimalMin(value = "1.0", message = "Invalid Price: min price of $1.0")
  BigDecimal price
) {}