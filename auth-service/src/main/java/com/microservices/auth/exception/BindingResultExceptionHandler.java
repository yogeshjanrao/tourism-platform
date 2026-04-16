package com.microservices.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class BindingResultExceptionHandler {

    public void bindingException(BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            AuthServiceException authServiceException = new AuthServiceException();
            authServiceException.setCode(HttpStatus.FORBIDDEN.value());
            authServiceException.setMessage("Enter Valid Data !!");
            bindingResult.getFieldErrors().stream().forEach(bindingResults -> authServiceException.addError(bindingResults.getField(), bindingResults.getDefaultMessage()));
            throw authServiceException;
        }
    }

}
