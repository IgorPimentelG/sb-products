package com.sb.products.exceptions;

public class ResourceNotFoundException extends Exception {

	public static final String name = "ResourceNotFoundException";

	public ResourceNotFoundException(String identifier) {
		super("Resource with id (" + identifier + ") not exists.");
	}
}
