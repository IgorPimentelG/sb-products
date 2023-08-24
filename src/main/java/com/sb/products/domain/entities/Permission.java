package com.sb.products.domain.entities;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "permissions")
public class Permission implements GrantedAuthority, Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String role;

  public Permission() {}

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
  public String getAuthority() {
    return role;
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
