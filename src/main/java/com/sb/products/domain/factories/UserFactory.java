package com.sb.products.domain.factories;

import com.sb.products.domain.entities.User;

public abstract class UserFactory {
  public static User create(String id, String fullName, String email, String password) {
    return new User(
      id,
      fullName,
      email,
      password,
      true,
      true,
      true,
      true
    );
  }

  public static User create(String fullName, String email, String password) {
    return new User(
      "",
      fullName,
      email,
      password,
      true,
      true,
      true,
      true
    );
  }

  public static User create(
    String id,
    String fullName,
    String email,
    String password,
    boolean isAccountNonExpired,
    boolean isAccountNonLocked,
    boolean isCredentialsNonExpired,
    boolean isEnabled
  ) {
    return new User(
      id,
      fullName,
      email,
      password,
      isAccountNonExpired,
      isAccountNonLocked,
      isCredentialsNonExpired,
      isEnabled
    );
  }
}
