package com.sb.products.data.errors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public record ExceptionResponse(String message, String details, Date timestamp) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
