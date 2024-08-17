package com.jeulog.controller;
import com.jeulog.request.PostCreate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

// SSR -> jsp, thymeleaf
        // -> html rendering
// SPA -> vue
        // -> javascript + <-> API (JSON)

@Slf4j
@RestController
public class PostController {
    @PostMapping("/posts")
    public Map<String, String> get(@Valid @RequestBody PostCreate dto, BindingResult result){
        log.info("dto={}", dto);

        if(result.hasErrors()){
            Map<String, String> error = new HashMap<>();
            List<FieldError> fieldErrors = result.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                error.put(fieldName, errorMessage);
            }
            return error;
        }
        return Map.of();
    }
}
