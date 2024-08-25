package com.jeulog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import com.jeulog.request.PostEdit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
    @DisplayName("글 작성 요청시 title, content 필수")
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
    @DisplayName("글 작성 요청시 DB에 값이 저장된다.")
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
                        .header("authorization", "jeu")
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
        mockMvc.perform(get("/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$.[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$.[9].title").value("호돌맨 제목 21"))
                .andExpect(jsonPath("$.[0].content").value("배제우 내용 30"))
                .andExpect(jsonPath("$.[9].content").value("배제우 내용 21"))
                .andDo(print());
    }
    @Test
    @DisplayName("글 수정")
    void test8() throws Exception{
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                       .title("호돌걸")
                       .content("자이반포")
                       .build();

        String json = objectMapper.writeValueAsString(postEdit);

        // expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception{
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test10() throws Exception{
        // expected
        mockMvc.perform(get("/posts/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test11() throws Exception{
        // given
        PostEdit postEdit = PostEdit.builder()
                      .title("호돌걸")
                      .content("자이반포")
                      .build();

        String json = objectMapper.writeValueAsString(postEdit);

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void tet12() throws Exception{
       // expected
       mockMvc.perform(delete("/posts/{postId}", 1)
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value("존재하지 않는 글입니다."))
               .andExpect(jsonPath("$.code").value("404"))
               .andExpect(jsonPath("$.validations.size()").value(0))
               .andDo(print());
    }
    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void tet13() throws Exception{
        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보")
                .content("반포자이")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
         mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."))
                .andDo(print());
    }
}