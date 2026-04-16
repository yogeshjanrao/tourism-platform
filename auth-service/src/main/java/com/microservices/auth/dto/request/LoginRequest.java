package com.microservices.auth.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @Valid
    @NotNull
    @NotEmpty
    private String email;

    @Valid
    @NotNull
    @NotEmpty
    private String password;
}