package com.sb.products.main.config.handles;

import com.sb.products.data.errors.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@RestController
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleDefaultException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.INTERNAL_SERVER_ERROR
		);
	}

	@ExceptionHandler(NotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.NOT_FOUND
		);
	}

	@ExceptionHandler(RequiredException.class)
	public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.BAD_REQUEST
		);
	}

	@ExceptionHandler(ConflictException.class)
	public final ResponseEntity<ExceptionResponse> handleConflictException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.CONFLICT
		);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<ExceptionResponse> handleUnauthorizedException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.UNAUTHORIZED
		);
	}

	@ExceptionHandler(FileStorageException.class)
	public final ResponseEntity<ExceptionResponse> handleInternalErrorException(Exception ex, WebRequest request) {
		return new ResponseEntity<>(
		  responseFactory(ex, request),
		  HttpStatus.INTERNAL_SERVER_ERROR
		);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
	  MethodArgumentNotValidException ex,
	  HttpHeaders headers,
	  HttpStatusCode status,
	  WebRequest request) {

		List<String> errors = ex.getBindingResult()
		  .getFieldErrors()
		  .stream()
		  .map(FieldError::getDefaultMessage)
		  .toList();

		ExceptionResponse response = new ExceptionResponse(
		  "Invalid fields: " + Arrays.toString(errors.toArray()),
		  new Date()
		);

		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	private ExceptionResponse responseFactory(Exception ex, WebRequest request) {
		return new ExceptionResponse(
		  ex.getMessage(),
		  new Date()
		);
	}
}
