package com.sb.products.infra.controller.dtos;

import java.util.List;

public class UserDto {
	public String id;
	public String fullName;
	public String email;
	public List<String> permissions;
	public boolean enabled;
	public boolean accountNonExpired;
	public boolean accountNonLocked;
	public boolean credentialsNonExpired;
}
