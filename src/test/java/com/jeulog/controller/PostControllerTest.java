package com.jeulog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.awt.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @AutoConfigureMockMvc
@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("/posts 요청시 Hello World 출력")
    void test() throws Exception {
        // expected
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }

    @Test
    @DisplayName("application/x-www-form-urlencoded")
    void test2() throws Exception{
        mockMvc.perform(
                post("/posts")
                        //.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", "글 제목 입니다")
                        .param("content", "글 내용 입니다"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
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
                .andExpect(content().string("Hello World"))
                .andDo(print());
    }
    @Test
    @DisplayName("/post 요청시 title 필수")
    void test4() throws Exception{
        mockMvc.perform(
                post("/posts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{\"title\": \"\", \"content\": \"내용입니다.\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

}