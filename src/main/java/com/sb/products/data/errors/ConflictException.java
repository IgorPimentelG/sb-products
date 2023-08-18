package com.sb.products.data.errors;

public class ConflictException extends Exception {

	public static final String name = "ConflictException";

	public ConflictException(String message) {
		super(message);
	}
}
