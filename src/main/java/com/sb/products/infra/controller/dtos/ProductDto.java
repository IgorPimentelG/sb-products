package com.sb.products.infra.controller.dtos;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record ProductDto(
  @NotBlank(message = "Invalid Name: empty name")
  @Length(min = 3, max = 255, message = "Invalid name: must be of 3 - 255 characters")
  String name,

  @Length(max = 500, message = "Invalid Description: must be a max of 500 characters")
  String description,

  @NotNull(message = "Invalid Price: empty price")
  @DecimalMin(value = "1.0", message = "Invalid Price: min price of $1.0")
  double price,

  @NotEmpty(message = "Invalid Barcode: empty barcode")
  @Pattern(regexp = "^[0-9]*$", message = "Invalid Barcode: only numbers")
  String barcode
) {}
