package com.jeulog.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.*;

/**
 * {
 *     "code": "400",
 *     "message": "잘못된 요청입니다.",
 *     "validation":{
 *         "title": "값을 입력해주세요"
 *     }
 * }
 */
@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final List<Validation> validations = new ArrayList<>();
    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
    public void addValidation(String fieldName, String errorMessage){
        validations.add(new Validation(fieldName, errorMessage));
    }
    @Getter
    @RequiredArgsConstructor
    private class Validation {
        private final String fieldName;
        private final String errorMessage;
    }
}
