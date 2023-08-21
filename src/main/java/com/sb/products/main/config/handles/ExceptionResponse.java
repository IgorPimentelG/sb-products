package com.sb.products.main.config.handles;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public record ExceptionResponse(String message, Date timestamp) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}
