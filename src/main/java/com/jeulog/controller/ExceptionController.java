package com.jeulog.controller;

import com.jeulog.exception.PostNotFound;
import com.jeulog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

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
                .message("잘못된 요청입니다.")
                .build();

        e.getFieldErrors().stream().forEach(o -> {
            response.addValidation(o.getField(), o.getDefaultMessage());
        });

        return response;
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse postNotFound(PostNotFound e){
        return ErrorResponse.builder()
                .code("404")
                .message(e.getMessage())
                .build();
    }

}
