package com.sb.products.infra.controller.docs.auth;

import com.sb.products.infra.controller.dtos.AuthDto;
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
@Operation(summary = "Refresh Access Token", description = "Returns the user with updated access token", tags = {"Auth"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "Access token updated successfully",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = AuthDto.class))
  }),
  @ApiResponse(responseCode = "400", description = "Token not found in header",
	content = {
	  @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
	}),
  @ApiResponse(responseCode = "404", description = "User not found",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "500", description = "An internal error occurred on the server",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface RefreshTokenDoc {}

