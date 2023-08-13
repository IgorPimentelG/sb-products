package com.sb.products.exceptions;

public class ResourceRequiredException extends Exception {

	public static final String name = "ResourceRequiredException";

	public ResourceRequiredException() {
		super("Resource cannot be null.");
	}
}
