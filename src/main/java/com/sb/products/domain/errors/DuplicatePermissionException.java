package com.sb.products.domain.errors;

public class DuplicatePermissionException extends Exception {

  public static final String name = "DuplicatePermissionException";

  public DuplicatePermissionException(String permission) {
    super("Permission (" + permission + ") already exists");
  }
}
