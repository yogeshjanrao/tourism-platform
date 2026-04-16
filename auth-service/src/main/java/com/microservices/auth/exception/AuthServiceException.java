package com.microservices.auth.exception;

import com.microservices.auth.dto.request.ResponseErrorDTO;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class AuthServiceException extends RuntimeException {
    private int code;
    private List<ResponseErrorDTO> errorList;
    private String message;

    public AuthServiceException() {
    }

    public AuthServiceException(int code, List<ResponseErrorDTO> errorList, String message) {
        this.code = code;
        this.errorList = errorList;
        this.message = message;
    }

    public AuthServiceException(HttpStatus code, String fieldName, String errorMassage, String message) {
        this.code = code.value();
        addError(fieldName,errorMassage );
        this.message = message;

    }

    public AuthServiceException(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }

    public void addError(String fieldName, String errorMassage){
        if (this.errorList == null){
            this.errorList = new ArrayList<>();
        }
        this.errorList.add(new ResponseErrorDTO(fieldName, errorMassage));
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ResponseErrorDTO> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<ResponseErrorDTO> errorList) {
        this.errorList = errorList;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
