package com.jeulog.controller;
import com.jeulog.domain.Post;
import com.jeulog.request.PostCreate;
import com.jeulog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping("/posts")
    public Post post(@Valid @RequestBody PostCreate request){
        log.info("request={}", request);
        // Case1. 저장한 데이터 Entity -> response
        // Case2. 저장한 데이터 primary_id -> response
        //        Client 수신한 id를 글 조회 API 통해서 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context 잘 관리함
        return postService.write(request);
    }
    @GetMapping("/posts/{postId}")
    public Post get(@PathVariable(name = "postId") Long id){
        log.info("get id = {}", id);
        return postService.get(id);
    }
}
