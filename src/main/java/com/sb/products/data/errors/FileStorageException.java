package com.sb.products.data.errors;

public class FileStorageException extends Exception {

	public static final String name = "FileStorageException";

	public FileStorageException() {
		super("Unable to store the file.");
	}
}
