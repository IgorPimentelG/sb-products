package com.sb.products.infra.controller.docs.file;

import com.sb.products.main.config.handles.ExceptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Download file", description = "Returns the file", tags = {"File"})
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "File was successfully downloaded",
	content = {
		@Content(mediaType = "application/octet-stream", schema = @Schema(implementation = Resource.class))
  }),
  @ApiResponse(responseCode = "404", description = "File does not exist",
	content = {
		@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))
  }),
})
public @interface DownloadDoc {}

