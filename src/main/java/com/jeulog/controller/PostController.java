package com.jeulog.controller;
import com.jeulog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
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
    public String get(@RequestBody PostCreate dto){
        log.info("dto={}", dto);
        return "Hello World";
    }
}
