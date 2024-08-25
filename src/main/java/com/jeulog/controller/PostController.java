package com.jeulog.controller;
import com.jeulog.domain.Post;
import com.jeulog.exception.InvalidRequest;
import com.jeulog.request.PostCreate;
import com.jeulog.request.PostEdit;
import com.jeulog.request.PostSearch;
import com.jeulog.response.PostResponse;
import com.jeulog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/test")  // 인증 필요
    public String test(){
        return "hello";
    }
    @GetMapping("/test2") // 인증 필요 X
    public String test2(){
        return "hello2";
    }


    @PostMapping("/posts")
    public Post post(@Valid @RequestBody PostCreate request, @RequestHeader String authorization){
        if(authorization.equals("jeu")){
            request.validate(request);
            return postService.write(request);
        }
        throw new InvalidRequest();
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
    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit postEdit){
        postService.edit(postId, postEdit);
    }
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }
}
