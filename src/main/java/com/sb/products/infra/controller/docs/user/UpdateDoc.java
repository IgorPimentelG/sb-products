package com.sb.products.infra.controller.docs.user;

import com.sb.products.domain.entities.User;
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
@Operation(summary = "Update a user [ONLY ADMIN AND USER]", description = "Returns the updated user", tags = {"User"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "User updated successfully",
	content = {
	  @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
  }),
  @ApiResponse(responseCode = "400", description = "User with data not accepted",
	content = {
	  @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
  @ApiResponse(responseCode = "401", description = "Invalid credentials to edit user",
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
public @interface UpdateDoc {}

