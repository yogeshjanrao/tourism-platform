package com.microservices.auth.service.impl;

import com.microservices.auth.dto.request.LoginRequest;
import com.microservices.auth.dto.request.RegisterRequest;
import com.microservices.auth.dto.response.RegisterResponse;
import com.microservices.auth.entity.Role;
import com.microservices.auth.entity.User;
import com.microservices.auth.exception.AuthServiceException;
import com.microservices.auth.repository.UserRepository;
import com.microservices.auth.service.AuthService;
import com.microservices.auth.util.JwtUtil;

import com.microservices.auth.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  JwtUtil jwtUtil;
    @Autowired
    private  PasswordEncoder passwordEncoder;


    @Override
    public RegisterResponse register(RegisterRequest request) {

        //Validation
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new AuthServiceException(HttpStatus.BAD_REQUEST, "User already registered");
                });

        //Validate Request
        ValidationUtil.validateRegisterDto(request);

        //Create user
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setContactNo(request.getContactNo());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        //Set Roles
        ArrayList<Role> roles = new ArrayList<>();
        roles.add(new Role("USER"));
        user.setRoles(roles);

        //Save user
        user = userRepository.save(user);

        return new RegisterResponse(HttpStatus.OK.value(), "User registered successfully", user.getEmail(), user.getContactNo());
    }

    @Override
    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getIsEmailVerified()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "User email is not verified");
        }

        if (!user.getIsContactVerified()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "User contact is not verified");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user);
    }
}