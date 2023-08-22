package com.sb.products.data.gateway;

import com.sb.products.data.errors.NotFoundException;
import com.sb.products.data.errors.RequiredException;
import com.sb.products.data.errors.UnauthorizedException;
import com.sb.products.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserGateway {
	User update(String id, User user) throws NotFoundException, RequiredException, UnauthorizedException;
	User findById(String id) throws NotFoundException;
	Page<User> findAll(Pageable pageable);
	void delete(String id) throws NotFoundException;
	User disable(String id) throws NotFoundException, UnauthorizedException;
}
