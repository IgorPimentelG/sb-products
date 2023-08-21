package com.sb.products.infra.controller.docs.auth;

import com.sb.products.infra.controller.dtos.UserDto;
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
@Operation(summary = "Resister common User", description = "Returns the user with access token", tags = {"Auth"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "User registered successfully",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
  }),
  @ApiResponse(responseCode = "400", description = "The body has invalid fields",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "500", description = "An internal error occurred on the server",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface CommonRegisterDoc {}

