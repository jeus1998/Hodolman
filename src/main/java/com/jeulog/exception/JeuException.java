package com.jeulog.exception;

import com.jeulog.response.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

public abstract class JeuException extends RuntimeException {
    private final List<Validation> validations = new ArrayList<>();
    public JeuException(String message) {
        super(message);
    }
    public List<Validation> getValidations(){
        return validations;
    }
    public abstract int statusCode();
    public void addValidations(String fieldName, String errorMessage){
        validations.add(Validation.builder()
                        .fieldName(fieldName)
                        .errorMessage(errorMessage)
                        .build());
    }
}
