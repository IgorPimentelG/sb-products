package com.sb.products.infra.controller.docs.product;

import com.sb.products.domain.entities.Product;
import com.sb.products.main.config.handles.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Register a product", description = "Returns the created product with id", tags = {"Product"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "Product created successfully",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))
  }),
  @ApiResponse(responseCode = "400", description = "Product with data not accepted",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "500", description = "An internal error occurred on the server",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface CreateDoc {}

