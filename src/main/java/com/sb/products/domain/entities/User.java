package com.sb.products.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sb.products.domain.errors.DuplicatePermissionException;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;
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
public class User extends RepresentationModel<User> implements Serializable, UserDetails {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String fullName;
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
  private final List<Permission> permissions;

  public User() {
    setEnabled(true);
    setAccountNonExpired(true);
    setAccountNonLocked(true);
    setCredentialsNonExpired(true);

    this.permissions = new ArrayList<>();
  }

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

  public User(
    String id,
    String fullName,
    String password,
    boolean isEnabled,
    boolean isAccountNonLocked,
    boolean isCredentialsNonExpired,
    boolean isAccountNonExpired
    ) {
    setId(id);
    setFullName(fullName);
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

  @JsonIgnore
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

  public List<String> getRoles() {
    return permissions.stream().map(Permission::getRole).toList();
  }

  public List<Permission> getPermissions() {
    return permissions;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return permissions;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof User user))
      return false;

    return Objects.equals(id, user.id)
        && Objects.equals(email, user.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email);
  }
}
