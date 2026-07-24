package com.erp.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class RegisterUserDataDTO {
	
	    private String email;
	    private String password;
	    private String token;

}
