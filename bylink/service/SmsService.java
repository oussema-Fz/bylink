package com.bylink.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    public void sendVerificationCode(String phoneNumber, String code) {
        Twilio.init(accountSid, authToken);
        
        Message.creator(
            new PhoneNumber(phoneNumber),
            new PhoneNumber(twilioPhoneNumber),
            "Your Bylink verification code is: " + code
        ).create();
    }

    public String generateVerificationCode() {
        // Generate a 6-digit code
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
}
