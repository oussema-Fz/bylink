package com.bylink.dto.auth;

import com.bylink.entity.UserRole;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String phoneNumber;
    private UserRole role;
    private boolean verified;
}
