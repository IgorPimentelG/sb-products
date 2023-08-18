package com.sb.products.data.errors;

public class UnauthorizedException extends Exception {

	public static final String name = "UnauthorizedException";

	public UnauthorizedException() {
		super("User is not allowed to access this application.");
	}
}
