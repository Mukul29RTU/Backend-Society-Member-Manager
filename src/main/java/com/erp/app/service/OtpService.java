package com.erp.app.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erp.app.entities.EmailOtp;
import com.erp.app.repository.EmailOtpRepo;
import com.erp.app.utility.OtpGenerator;

import jakarta.transaction.Transactional;

@Service
public class OtpService {

	@Autowired
	private EmailOtpRepo repoOtp;
	
	@Autowired
	PasswordEncoder otpEncoder;
	
	public String generateOtp(String email) {
		
		String otp = OtpGenerator.generateOtp();
		
		EmailOtp emailOtp = new EmailOtp();
		emailOtp.setEmail(email);
		
		String hashedOTP = otpEncoder.encode(otp);
		emailOtp.setOtp(hashedOTP);
		emailOtp.setExpiryTime(LocalDateTime.now().plusMinutes(20));
	
		
		repoOtp.save(emailOtp);
		return otp;
	}
	

	@Transactional
	public boolean validateOtp(String email, String otp) {
	    EmailOtp savedOtp = repoOtp.findTopByEmailOrderByExpiryTimeDesc(email).orNull();
	    
	    if (savedOtp == null) {
	    	throw new RuntimeException("No OTP found for this email. Please request a new one.");
	    }
	    
	    if (savedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            repoOtp.delete(savedOtp); // Clean up expired record
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }
	    
	    if (!otpEncoder.matches(otp, savedOtp.getOtp())) {
	    	throw new RuntimeException("Invalid OTP provided.");
	    }

	    repoOtp.delete(savedOtp); 
	    return true;
	}
}
