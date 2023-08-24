package com.sb.products.infra.repositories;

import com.sb.products.domain.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Permission findByRole(String role);
}
