package com.bylink.controller;

import com.bylink.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/send-verification")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestParam String phoneNumber) {
        try {
            String code = smsService.generateVerificationCode();
            smsService.sendVerificationCode(phoneNumber, code);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Verification code sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to send verification code: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
