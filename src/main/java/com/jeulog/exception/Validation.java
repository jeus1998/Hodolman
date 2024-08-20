package com.jeulog.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Validation {
    private String fieldName;
    private String errorMessage;
    @Builder
    public Validation(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
