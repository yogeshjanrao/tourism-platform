package com.microservices.auth.util;

import com.microservices.auth.dto.request.RegisterRequest;
import com.microservices.auth.exception.AuthServiceException;
import org.springframework.http.HttpStatus;

public class ValidationUtil {

    public static void validateRegisterDto(RegisterRequest request) {
        if (request.getFirstName().isEmpty() || request.getFirstName().isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "First Name is Required");
        }
        if (request.getLastName().isEmpty() || request.getLastName().isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Last Name is Required");
        }
        if (request.getEmail().isEmpty() || request.getEmail().isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Email is Required");
        }
        if (request.getPassword().isEmpty() || request.getPassword().isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Password is Required");
        }
        if (request.getContactNo().isEmpty() || request.getContactNo().isBlank()) {
            throw new AuthServiceException(HttpStatus.BAD_REQUEST, "Contact No is Required");
        }
    }
}
