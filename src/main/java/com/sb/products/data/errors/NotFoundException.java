package com.sb.products.data.errors;

public class NotFoundException extends Exception {

	public static final String name = "ResourceNotFoundException";

	public NotFoundException(String identifier) {
		super("Resource with id (" + identifier + ") not exists.");
	}
}
