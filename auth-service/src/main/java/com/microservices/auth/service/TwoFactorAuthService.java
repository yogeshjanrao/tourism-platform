package com.microservices.auth.service;

import com.microservices.auth.dto.response.ResponseDTO;

public interface TwoFactorAuthService {
    ResponseDTO sendEmailOtp(String email);
    ResponseDTO  verifyEmailOtp(String email, String otp);
    String sendContactOtp(String contact);
    String verifyContactOtp(String contact, String otp);
}
