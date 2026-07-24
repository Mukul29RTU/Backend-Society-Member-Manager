package com.erp.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.app.DTO.ApiResponse;
import com.erp.app.DTO.ForgotPasswordOtpDTO;
import com.erp.app.DTO.LoginUserDataDTO;
import com.erp.app.DTO.RegisterUserDataDTO;
import com.erp.app.DTO.RegisterUserOtpDTO;
import com.erp.app.DTO.ResetPasswordDTO;
import com.erp.app.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register/sendotp")
	public ResponseEntity<ApiResponse<String>> sendRegisterOtp(@RequestBody RegisterUserOtpDTO body) {
		
		String result = userService.registerOtp(body.getEmail());
		return ResponseEntity.ok(ApiResponse.success("OTP SENT", result));
	}

	@PostMapping("/register/validateotp")
	public ResponseEntity<ApiResponse<String>> validateOtp(@RequestBody RegisterUserOtpDTO object) {
		String token = userService.verifyRegisterOTP(object);
		return ResponseEntity.ok(ApiResponse.success("OTP Verified Succesfully", token));
	}

	@PostMapping("/register/adduser")
	public ResponseEntity<ApiResponse<String>> addUser(@RequestBody RegisterUserDataDTO object) {
		String message = userService.addRegisterUserData(object);
		return ResponseEntity.ok(ApiResponse.success(message, null));
	}

	@PostMapping("/forgot-password/sendotp")
	public ResponseEntity<ApiResponse<String>> sendForgotPasswordOtp(@RequestBody ForgotPasswordOtpDTO body) {
		String result = userService.forgotPasswordOtp(body.getEmail());
		return ResponseEntity.ok(ApiResponse.success("OTP SENT", result));
	}

	@PostMapping("/forgot-password/validateotp")
	public ResponseEntity<ApiResponse<String>> validateForgotPasswordOtp(@RequestBody RegisterUserOtpDTO object) {
		String token = userService.verifyForgotPasswordOTP(object);
		return ResponseEntity.ok(ApiResponse.success("OTP Verified Successfully", token));
	}

	@PostMapping("/forgot-password/reset")
	public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordDTO object) {
		String message = userService.resetPassword(object);
		return ResponseEntity.ok(ApiResponse.success(message, null));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<java.util.Map<String, String>>> login(@RequestBody LoginUserDataDTO loginObject) {
		java.util.Map<String, String> loginData = userService.loginUserRequest(loginObject);
		return ResponseEntity.ok(ApiResponse.success("Login Successful", loginData));
	}
}
