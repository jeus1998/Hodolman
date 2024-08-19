package com.jeulog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
// WebMvcTest
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("application/x-www-form-urlencoded")
    void test1() throws Exception{
        mockMvc.perform(
                post("/posts")
                        //.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목 입니다")
                        .param("content", "글 내용 입니다"))
                .andExpect(status().isUnsupportedMediaType()) // 415 @RequestBody 사용중
                .andDo(print());
    }
    @Test
    @DisplayName("application/json")
    void test2() throws Exception{
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request); // ObjectMapper 활용 json 형태로 가공
        System.out.println("json = " + json);

        // expected
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목입니다."))
                .andExpect(jsonPath("$.content").value("내용입니다."))
                .andDo(print());
    }
    @Test
    @DisplayName("/post 요청시 title, content 필수")
    void test3() throws Exception{

        PostCreate request = PostCreate.builder().build();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validations.size()").value(2))
                .andDo(print());
    }
    @Test
    @DisplayName("/post 요청시 DB에 값이 저장된다.")
    void test4() throws Exception{
        // when

        PostCreate request = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(postRepository.count()).isEqualTo(1);

        List<Post> result = postRepository.findAll();
        assertThat(result).extracting("title")
                .containsExactly("제목입니다.");
    }
    @Test
    @DisplayName("글 1개 조회")
    void test5() throws Exception{
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("foo"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andDo(print());
    }
    @Test
    @DisplayName("글 N개 조회")
    void test6() throws Exception{
        // given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        Post post2 = Post.builder()
                .title("foo2")
                .content("bar2")
                .build();
        postRepository.saveAll(List.of(post, post2));

        // expected
        mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.length()", Matchers.is(2))) // Matcher 인터페이스를 구현해서 기능 확장이 가능
                .andExpect(jsonPath("$.[0].title").value("foo"))
                .andExpect(jsonPath("$.[0].content").value("bar"))
                .andExpect(jsonPath("$.[0].id").value(post.getId()))
                .andExpect(jsonPath("$.[1].title").value("foo2"))
                .andExpect(jsonPath("$.[1].content").value("bar2"))
                .andExpect(jsonPath("$.[1].id").value(post2.getId()))
                .andDo(print());
    }
    @Test
    @DisplayName("글 조회(페이징)")
    void test7() throws Exception{
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
               .mapToObj(i -> {
                   return Post.builder()
                           .title("호돌맨 제목 " + i)
                           .content("배제우 내용 " + i)
                           .build();
               })
               .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        // Pageable 파라미터 ex page=1&size=10&sort=id,desc
        mockMvc.perform(get("/posts?page=1&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$.[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$.[9].title").value("호돌맨 제목 21"))
                .andExpect(jsonPath("$.[0].content").value("배제우 내용 30"))
                .andExpect(jsonPath("$.[9].content").value("배제우 내용 21"))
                .andDo(print());
    }
}