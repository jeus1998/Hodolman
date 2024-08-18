package com.jeulog.service;

import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Test
    @DisplayName("글 작성")
    void test1(){
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        postService.write(postCreate);

        // then
        assertThat(1L).isEqualTo(postRepository.count());
        List<Post> result = postRepository.findAll();
             assertThat(result).extracting("title")
                     .containsExactly("제목");
    }
}