package com.sb.products.infra.database.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class UserSchema implements Serializable, UserDetails {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String fullName;
  private String email;

  @JsonProperty(access = Access.WRITE_ONLY)
  private String password;

  @Column(name = "account_non_expired")
  private boolean isAccountNonExpired;

  @Column(name = "account_non_locked")
  private boolean isAccountNonLocked;

  @Column(name = "credentials_non_expired")
  private boolean isCredentialsNonExpired;

  @Column(name = "enabled")
  private boolean isEnabled;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_permissions", 
    joinColumns = {@JoinColumn(name = "id_user")},
    inverseJoinColumns = {@JoinColumn(name = "id_permission")}
  )
  private final List<PermissionSchema> permissions;

  public UserSchema() {
    this.permissions = new ArrayList<>();
  }

  public UserSchema(
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

  public void addPermission(PermissionSchema permission) {
    permissions.add(permission);
  }

  public List<String> getRoles() {
    return permissions.stream().map(PermissionSchema::getRole).toList();
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

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
  
  @JsonIgnore
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
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

  public List<PermissionSchema> getPermissions() {
    return permissions;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) return true;
    if (!(o instanceof UserSchema user)) return false;

    return Objects.equals(id, user.id)
        && Objects.equals(fullName, user.fullName)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return permissions;
  }

  @Override
  public String getUsername() {
    return email;
  }
}
