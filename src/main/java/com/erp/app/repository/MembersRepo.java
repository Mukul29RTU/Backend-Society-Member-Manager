package com.erp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.erp.app.entities.Members;
import com.google.common.base.Optional;

@Repository
public interface MembersRepo extends JpaRepository<Members, Integer> {
	
	@Query(value = "Select * from members WHERE email = :email", nativeQuery = true)
	Optional<Members> findByEmail(@Param("email") String email);
	
	
	
}
