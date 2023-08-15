package com.sb.products.data.errors;

public class RequiredException extends Exception {

	public static final String name = "ResourceRequiredException";

	public RequiredException() {
		super("Resource cannot be null.");
	}
}
