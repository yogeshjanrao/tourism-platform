package com.microservices.auth.service;

import com.microservices.auth.dto.request.LoginRequest;
import com.microservices.auth.dto.request.RegisterRequest;
import com.microservices.auth.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    String login(LoginRequest request);
}