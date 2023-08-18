package com.sb.products.infra.database.schemas;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "permissions")
public class PermissionSchema implements GrantedAuthority, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String role;

  public PermissionSchema() {}

  public PermissionSchema(int id, String role) {
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
    if (!(o instanceof PermissionSchema permission)) return false;

    return id == permission.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, role);
  }

  @Override
  public String getAuthority() {
    return role;
  }
}
