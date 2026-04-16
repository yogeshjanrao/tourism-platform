package com.microservices.auth.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtil {
    public String generateOtp() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }
}
