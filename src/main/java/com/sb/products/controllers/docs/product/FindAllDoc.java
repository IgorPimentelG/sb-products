package com.sb.products.controllers.docs.product;

import com.sb.products.exceptions.ExceptionResponse;
import com.sb.products.models.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Operation(summary = "Find all product", description = "Returns all created already products", tags = {"Product"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "List of all products",
	content = {
		@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))
  }),
  @ApiResponse(responseCode = "400", description = "Params not accepted",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "500", description = "An internal error occurred on the server",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface FindAllDoc {}

