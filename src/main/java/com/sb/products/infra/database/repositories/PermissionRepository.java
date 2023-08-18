package com.sb.products.infra.database.repositories;

import com.sb.products.infra.database.schemas.PermissionSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionSchema, Long> {
	PermissionSchema findByRole(String role);
}
