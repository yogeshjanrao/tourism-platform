package com.microservices.auth.controller;

import com.microservices.auth.dto.request.LoginRequest;
import com.microservices.auth.dto.request.RegisterRequest;
import com.microservices.auth.dto.response.AuthResponse;
import com.microservices.auth.dto.response.RegisterResponse;
import com.microservices.auth.dto.response.ResponseDTO;
import com.microservices.auth.exception.BindingResultExceptionHandler;
import com.microservices.auth.service.AuthService;
import com.microservices.auth.service.TwoFactorAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @Autowired
    private BindingResultExceptionHandler bindingResultExceptionHandler;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResultExceptionHandler.bindingException(bindingResult);
        }
        RegisterResponse registered = authService.register(request);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/email/send-otp")
    public ResponseEntity<?> sendEmailOtp(@RequestParam("email") String email) {
        ResponseDTO sent = twoFactorAuthService.sendEmailOtp(email);
        return ResponseEntity.ok(sent);
    }

    @PostMapping("/email/send-otp/verify")
    public ResponseEntity<?> verifyEmailOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        ResponseDTO verifyEmailOtp = twoFactorAuthService.verifyEmailOtp(email, otp);
        return ResponseEntity.ok(verifyEmailOtp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResultExceptionHandler.bindingException(bindingResult);
        }
        String login = authService.login(request);
        return ResponseEntity.ok(new AuthResponse(login));
    }
}