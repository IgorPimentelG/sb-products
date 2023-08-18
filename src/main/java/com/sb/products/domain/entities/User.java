package com.sb.products.domain.entities;

import com.sb.products.domain.errors.DuplicatePermissionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {

  private String id;
  private String fullName;
  private String email;
  private String password;
  private boolean isAccountNonExpired;
  private boolean isAccountNonLocked;
  private boolean isCredentialsNonExpired;
  private boolean isEnabled;
  private final List<Permission> permissions;

  public User(
      String id,
      String fullName,
      String email,
      String password,
      boolean isAccountNonExpired,
      boolean isAccountNonLocked,
      boolean isCredentialsNonExpired,
      boolean isEnabled) {
    setId(id);
    setFullName(fullName);
    setEmail(email);
    setPassword(password);
    setAccountNonExpired(isAccountNonExpired);
    setAccountNonLocked(isAccountNonLocked);
    setCredentialsNonExpired(isCredentialsNonExpired);
    setEnabled(isEnabled);

    this.permissions = new ArrayList<>();
  }

  public void addPermission(Permission permission) throws DuplicatePermissionException {

    var duplicatePermissions = permissions.stream()
        .filter((item) -> item.equals(permission))
        .toList();

    if (!duplicatePermissions.isEmpty()) {
      throw new DuplicatePermissionException(permission.getRole());
    }

    permissions.add(permission);
  }

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setFullName(String fullName) throws IllegalArgumentException {

    if (fullName.isEmpty() || fullName.isBlank()) {
      throw new IllegalArgumentException("Full Name cannot be empty");
    }

    this.fullName = fullName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) throws IllegalArgumentException {

    var rgx = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    if (email.isEmpty() || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be empty");
    } else if (!email.matches(rgx)) {
      throw new IllegalArgumentException("Invalid email address");
    }

    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) throws IllegalArgumentException {

    if (password.isEmpty() || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be empty");
    } else if (password.indexOf(" ") > 0) {
      throw new IllegalArgumentException("Password cannot has empty characters");
    } else if (password.length() < 8) {
      throw new IllegalArgumentException("Password cannot be less than 8 characters");
    }

    this.password = password;
  }

  public void setAccountNonExpired(boolean isAccountNonExpired) {
    this.isAccountNonExpired = isAccountNonExpired;
  }

  public boolean isAccountNonExpired() {
    return this.isAccountNonExpired;
  }

  public boolean isAccountNonLocked() {
    return this.isAccountNonLocked;
  }

  public void setAccountNonLocked(boolean isAccountNonLocked) {
    this.isAccountNonLocked = isAccountNonLocked;
  }

  public boolean isCredentialsNonExpired() {
    return this.isCredentialsNonExpired;
  }

  public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
    this.isCredentialsNonExpired = isCredentialsNonExpired;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }

  public List<Permission> getPermissions() {
    return permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof User user))
      return false;

    return Objects.equals(id, user.id)
        && Objects.equals(fullName, user.fullName)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email);
  }
}
