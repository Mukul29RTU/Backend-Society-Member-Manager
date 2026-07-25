package com.erp.app.aop;

import org.aspectj.lang.annotation.AfterReturning;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.erp.app.DTO.LoginUserDataDTO;
import com.erp.app.DTO.RegisterUserDataDTO;
import com.erp.app.service.EmailService;

@Aspect
@Component
public class EmailNotificationAspect {

    @Autowired
    private EmailService emailService;

    @AfterReturning(pointcut = "execution(* com.erp.app.service.UserService.addRegisterUserData(..)) && args(userDTO)", returning = "result")
    public void afterSuccessfulRegistration(RegisterUserDataDTO userDTO, Object result) {
        // Only send if the result is successful (UserService returns a success string)
        if (result instanceof String && ((String) result).contains("Successfully")) {
            String subject = "Welcome to Bari Agrawal Society!";
            String body = "Hello,\n\nYour account has been successfully created. Welcome to the community!\n\nRegards,\nAgrawal Society Team";
            emailService.sendEmail(userDTO.getEmail(), subject, body);
        }
    }

    @AfterReturning(pointcut = "execution(* com.erp.app.service.UserService.loginUserRequest(..)) && args(loginDTO)", returning = "token")
    public void afterSuccessfulLogin(LoginUserDataDTO loginDTO, String token) {
        // If execution reached here, it means authenticationManager.authenticate()
        // succeeded
        String subject = "New Login Detected";
        String body = "Hello,\n\nA new login was detected on your account at " + new java.util.Date()
                + ".\n\nIf this wasn't you, please secure your account.\n\nRegards,\nAgrawal Society Team, Bari";
        emailService.sendEmail(loginDTO.getEmail(), subject, body);
    }
}
