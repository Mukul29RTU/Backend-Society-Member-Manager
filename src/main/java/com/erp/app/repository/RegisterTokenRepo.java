package com.erp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.EmailOtp;
import com.erp.app.entities.RegisterToken;
import com.erp.app.entities.Users;
import com.google.common.base.Optional;

@Repository
public interface RegisterTokenRepo extends JpaRepository<RegisterToken, String>
{
//	@Query(value = "SELECT * FROM registertoken WHERE email = :email", nativeQuery = true)
	Optional<RegisterToken> findByEmail(String email);
}
