package com.sb.products.infra.repositories;

import com.sb.products.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	User findByEmail(String email);

	@Modifying
	@Query("UPDATE User user SET user.isEnabled = false WHERE user.id =:id")
	void disableUser(@Param("id") String id);
}
