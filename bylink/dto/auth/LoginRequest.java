package com.bylink.dto.auth;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;
}
