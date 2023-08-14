package com.sb.products.controller.docs.product;

import com.sb.products.exceptions.ExceptionResponse;
import com.sb.products.model.Product;
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
@Operation(summary = "Find a product", description = "Returns the product", tags = {"Product"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "Product found successfully",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))
  }),
  @ApiResponse(responseCode = "400", description = "Params not accepted",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "404", description = "Product not found",
	content = {
	  @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
	}),
  @ApiResponse(responseCode = "500", description = "An internal error occurred on the server",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface FindByIdDoc {}
