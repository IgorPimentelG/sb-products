package com.sb.products.exceptions;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private final String message;
	private final String details;
	private final Date timestamp;

	public ExceptionResponse(String message, String details, Date timestamp) {
		this.message = message;
		this.details = details;
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

	public Date getTimestamp() {
		return timestamp;
	}
}
