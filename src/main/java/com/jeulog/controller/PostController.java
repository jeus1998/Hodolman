package com.jeulog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// SSR -> jsp, thymeleaf
        // -> html rendering
// SPA -> vue
        // -> javascript + <-> API (JSON)
@RestController
public class PostController {
    @GetMapping("/posts")
    public String get(){
        return "Hello World";
    }
}
