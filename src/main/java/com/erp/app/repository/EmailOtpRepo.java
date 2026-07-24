package com.erp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.app.entities.EmailOtp;
import com.google.common.base.Optional;

@Repository
public interface EmailOtpRepo extends JpaRepository<EmailOtp, Long> {

	Optional<EmailOtp> findTopByEmailOrderByExpiryTimeDesc(String email);

	
}
