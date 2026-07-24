package com.erp.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RegisterUserOtpDTO {
    
    private String email;
    private String otp;
    
}

