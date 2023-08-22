package com.sb.products.infra.database.repositories;

import com.sb.products.infra.database.schemas.UserSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserSchema, String> {
	UserSchema findByEmail(String email);

	@Modifying
	@Query("UPDATE UserSchema u SET u.isEnabled = false WHERE u.id =:id")
	void disableUser(@Param("id") String id);
}
