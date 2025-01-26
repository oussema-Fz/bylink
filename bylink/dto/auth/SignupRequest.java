package com.bylink.dto.auth;

import com.bylink.entity.UserType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignupRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^[0-9]{8}$")
    private String phoneNumber;

    private String cinNumber;
    
    private String fiscalNumber;
    
    @NotBlank
    private String password;

    private UserType userType;
    
    private String companyName;
    
    private boolean hasPatente;
}
