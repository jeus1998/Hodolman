package com.jeulog.controller;

import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
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
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("application/x-www-form-urlencoded")
    void test2() throws Exception{
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
    void test3() throws Exception{
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print());
    }
    @Test
    @DisplayName("/post 요청시 title 필수")
    void test4() throws Exception{
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\": \"\", \"content\": null }")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validations[0].errorMessage").value("타이틀을 입력해주세요"))
                .andExpect(jsonPath("$.validations[1].errorMessage").value("컨텐트를 입력해주세요"))
                .andDo(print());
    }
    @Test
    @DisplayName("/post 요청시 DB에 값이 저장된다.")
    void test5() throws Exception{
        // when
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\" }")
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertThat(postRepository.count()).isEqualTo(1);

        List<Post> result = postRepository.findAll();
        assertThat(result).extracting("title")
                .containsExactly("제목입니다.");
    }

}