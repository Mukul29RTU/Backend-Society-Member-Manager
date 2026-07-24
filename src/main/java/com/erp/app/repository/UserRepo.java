package com.erp.app.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.Users;
import com.google.common.base.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer>{
	
	@Query(value = "Select * from users WHERE email = :email", nativeQuery = true)
	Optional<Users> findByEmail(@Param("email") String email);
}
