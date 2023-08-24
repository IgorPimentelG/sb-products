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
    var user = new User();
    user.setFullName(fullName);
    user.setEmail(email);
    user.setPassword(password);

    return user;
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

  public static User create(
    String id,
    String fullName,
    String password,
    boolean isEnabled,
    boolean isAccountNonLocked,
    boolean isCredentialsNonExpired,
    boolean isAccountNonExpired
  ) {
    var user = new User();

    if (fullName != null) user.setFullName(fullName);
    if (password != null) user.setPassword(password);

    user.setId(id);
    user.setEnabled(isEnabled);
    user.setAccountNonLocked(isAccountNonLocked);
    user.setAccountNonExpired(isAccountNonExpired);
    user.setCredentialsNonExpired(isCredentialsNonExpired);

    return user;
  }
}
