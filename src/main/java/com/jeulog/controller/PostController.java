package com.jeulog.controller;
import com.jeulog.domain.Post;
import com.jeulog.request.PostCreate;
import com.jeulog.request.PostSearch;
import com.jeulog.response.PostResponse;
import com.jeulog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping("/posts")
    public Post post(@Valid @RequestBody PostCreate request){
        log.info("request={}", request);
        return postService.write(request);
    }
    // 단건 조회 API
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        log.info("get id = {}", postId);
        return postService.get(postId);
    }
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch){
        return postService.getList(postSearch);
    }
}
