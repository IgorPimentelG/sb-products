package com.sb.products.domain.entities;

import java.util.Objects;

public class Permission {

  private int id;
  private String role;

  public Permission(int id, String role) {
    this.id = id;
    this.role = role;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof Permission permission)) return false;

    return id == permission.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, role);
  }
}
