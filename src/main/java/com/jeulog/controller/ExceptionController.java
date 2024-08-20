package com.jeulog.controller;

import com.jeulog.exception.JeuException;
import com.jeulog.exception.PostNotFound;
import com.jeulog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){
        // MethodArgumentNotValidException
        log.error("error", e);

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .validations(new ArrayList<>())
                .message("잘못된 요청입니다.")
                .build();

        e.getFieldErrors().stream().forEach(o -> {
            response.addValidation(o.getField(), o.getDefaultMessage());
        });

        return response;
    }
    /**
     * 예외 처리 공통화
     * ex) 예외 처리가 수백개면 수백개의 RuntimeException 만들고 컨트롤러 어드바이스에 적용해야함
     * JeuException abstract 클래스로 공통화
     */
    @ExceptionHandler(JeuException.class)
    public ResponseEntity<ErrorResponse> jeuException(JeuException e){

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.statusCode()))
                .validations(e.getValidations())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.statusCode()).body(body);
    }

}
