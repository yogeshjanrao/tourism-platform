package com.microservices.auth.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterRequest {

    @Valid
    @NotNull
    @NotEmpty
    private String firstName;

    @Valid
    @NotNull
    @NotEmpty
    private String lastName;

    @Valid
    @NotNull
    @NotEmpty
    private String email;

    @Valid
    @NotNull
    @NotEmpty
    private String password;

    @Valid
    @NotNull
    @NotEmpty
    private  String contactNo;


}
