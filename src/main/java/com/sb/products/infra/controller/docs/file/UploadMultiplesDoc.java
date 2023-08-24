package com.sb.products.infra.controller.docs.file;

import com.sb.products.domain.entities.Product;
import com.sb.products.infra.controller.dtos.UploadFileDto;
import com.sb.products.main.config.handles.ExceptionResponse;
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
@Operation(summary = "Upload multiple files", description = "Returns the files info", tags = {"File"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "Files was successfully uploaded",
	content = {
		@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UploadFileDto.class)))
  }),
  @ApiResponse(responseCode = "500", description = "Files could not be uploaded",
    content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  })
})
public @interface UploadMultiplesDoc {}

