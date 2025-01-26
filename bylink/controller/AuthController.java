package com.bylink.controller;

import com.bylink.dto.auth.AuthResponse;
import com.bylink.dto.auth.LoginRequest;
import com.bylink.dto.auth.SignupRequest;
import com.bylink.entity.User;
import com.bylink.security.JwtTokenProvider;
import com.bylink.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        User user = userService.registerUser(signupRequest);
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signupRequest.getPhoneNumber(),
                signupRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setVerified(user.isVerified());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getPhoneNumber(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        AuthResponse response = new AuthResponse();
        response.setToken(jwt);
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setVerified(user.isVerified());
        
        return ResponseEntity.ok(response);
    }
}
