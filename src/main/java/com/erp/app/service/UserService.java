package com.erp.app.service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erp.app.DTO.LoginUserDataDTO;
import com.erp.app.DTO.RegisterUserDataDTO;
import com.erp.app.DTO.RegisterUserOtpDTO;
import com.erp.app.DTO.ResetPasswordDTO;
import com.erp.app.entities.RegisterToken;
import com.erp.app.entities.Role;
import com.erp.app.entities.Users;
import com.erp.app.repository.MembersRepo;
import com.erp.app.repository.RegisterTokenRepo;
import com.erp.app.repository.RoleRepo;
import com.erp.app.repository.UserRepo;
import com.erp.app.security.CustomUserDetailService;
import com.erp.app.utility.JwtTokenUtil;

import jakarta.transaction.Transactional;

@Service
public class UserService {

	@Autowired
	private UserRepo repoUser;

	@Autowired
	private MembersRepo repoMember;

	@Autowired
	private RegisterTokenRepo repoToken;

	@Autowired
	private OtpService otpService;

	@Autowired
	private PasswordEncoder encoderPassword;

	@Autowired
	EmailService emailService;

	@Autowired
	private RoleRepo repoRole;

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtUtil;

	public String registerOtp(String email) {
		if (repoUser.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email is already registered!");
		}

		if (repoMember.findByEmail(email).isPresent()) {
			String otp = otpService.generateOtp(email);
			emailService.sendEmail(email, "Email Verification OTP", "Your OTP is: " + otp + "\nvalid for 5 minutes");
			return "OTP has been sent to your email";
		}

		throw new RuntimeException("Email not found in database. Access denied.");
	}

	public String verifyRegisterOTP(RegisterUserOtpDTO object) {
		boolean valid = otpService.validateOtp(object.getEmail(), object.getOtp());

		if (!valid) {
			throw new RuntimeException("Invalid or expired OTP. Please try again.");
		}

		String createToken = UUID.randomUUID().toString();
		RegisterToken myToken = new RegisterToken();
		myToken.setEmail(object.getEmail());
		myToken.setToken(createToken);
		repoToken.save(myToken);
		return createToken;
	}

	@Transactional
	public String addRegisterUserData(RegisterUserDataDTO object) {
		RegisterToken dbToken = repoToken.findByEmail(object.getEmail()).orNull();

		if (dbToken == null) {
			throw new RuntimeException("Registration session expired or email not found.");
		}

		if (!dbToken.getToken().equals(object.getToken())) {
			throw new RuntimeException("The provided registration token is invalid.");
		}

		Users newUser = new Users();
		newUser.setEmail(object.getEmail());
		newUser.setPassword(encoderPassword.encode(object.getPassword()));

		Role userRole = repoRole.findByName("ROLE_USER");
		if (userRole == null) {
			throw new RuntimeException("Default system role 'ROLE_USER' not found.");
		}
		Set<Role> roles = new HashSet<>();
		roles.add(userRole);
		newUser.setRoles(roles);

		repoUser.save(newUser);
		repoToken.delete(dbToken);

		return "User Added Successfully";
	}

	public String forgotPasswordOtp(String email) {
		if (!repoUser.findByEmail(email).isPresent()) {
			throw new RuntimeException("Email not found in database. Access denied.");
		}

		String otp = otpService.generateOtp(email);
		emailService.sendEmail(email, "Password Reset OTP", "Hello, Your OTP to reset your password is: " + otp + "\nvalid for 5 minutes.");
		return "OTP has been sent to your email";
	}

	public String verifyForgotPasswordOTP(RegisterUserOtpDTO object) {
		boolean valid = otpService.validateOtp(object.getEmail(), object.getOtp());

		if (!valid) {
			throw new RuntimeException("Invalid or expired OTP. Please try again.");
		}

		String resetToken = UUID.randomUUID().toString();
		RegisterToken myToken = new RegisterToken();
		myToken.setEmail(object.getEmail());
		myToken.setToken(resetToken);
		repoToken.save(myToken);
		return resetToken;
	}

	@Transactional
	public String resetPassword(ResetPasswordDTO object) {
		RegisterToken dbToken = repoToken.findByEmail(object.getEmail()).orNull();

		if (dbToken == null) {
			throw new RuntimeException("Password reset session expired or email not found.");
		}

		if (!dbToken.getToken().equals(object.getToken())) {
			throw new RuntimeException("The provided reset token is invalid.");
		}

		Users user = repoUser.findByEmail(object.getEmail()).orNull();
		if (user == null) {
			throw new RuntimeException("User not found");
		}
		user.setPassword(encoderPassword.encode(object.getNewPassword()));
		repoUser.save(user);

		repoToken.delete(dbToken);

		return "Password Reset Successfully";
	}

	public java.util.Map<String, String> loginUserRequest(LoginUserDataDTO loginObject) {
		try {
			// 1. This method internally checks:
			// - If user exists (throws UsernameNotFoundException)
			// - If password matches (throws BadCredentialsException)
			// - If account is locked/disabled (throws LockedException/DisabledException)
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginObject.getEmail(), loginObject.getPassword()));

			// 2. If we reach here, authentication was successful
			UserDetails userDetails = userDetailsService.loadUserByUsername(loginObject.getEmail());

			// 3. Generate the JWT
			String token = jwtUtil.generateToken(userDetails);
			
			// 4. Extract role
			String role = userDetails.getAuthorities().stream()
					.map(auth -> auth.getAuthority())
					.findFirst()
					.orElse("ROLE_USER");

			java.util.Map<String, String> responseData = new java.util.HashMap<>();
			responseData.put("token", token);
			responseData.put("role", role);
			
			return responseData;

		} catch (BadCredentialsException e) {
			// Re-throw or throw a custom exception that your Global Handler catches
			throw new BadCredentialsException("Invalid email or password");
		} catch (LockedException e) {
			throw new RuntimeException("User account is locked. Please contact support.");
		} catch (DisabledException e) {
			throw new RuntimeException("User account is disabled.");
		}
	}
}
