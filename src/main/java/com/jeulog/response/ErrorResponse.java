package com.jeulog.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jeulog.exception.Validation;
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
// @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private final String code;
    private final String message;
    private final List<Validation> validations;
    @Builder
    public ErrorResponse(String code, String message, List<Validation> validations) {
        this.validations = validations;
        this.code = code;
        this.message = message;
    }
    public void addValidation(String fieldName, String errorMessage){
        validations.add(new Validation(fieldName, errorMessage));
    }
}
