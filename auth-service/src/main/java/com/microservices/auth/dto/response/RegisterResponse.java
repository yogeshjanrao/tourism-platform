package com.microservices.auth.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    @NotNull
    @NotEmpty
    private int status;

    @NotNull
    @NotEmpty
    private String message;

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty
    private String contactNo;
}
