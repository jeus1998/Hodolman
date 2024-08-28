package com.jeulog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeulog.domain.Session;
import com.jeulog.domain.User;
import com.jeulog.repository.SessionRepository;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.Login;
import com.jeulog.request.SignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception{
        // given
        User user = User.builder()
                .email("baejeu@naver.com")
                .password("1234")
                .build();

        User saveUser = userRepository.save(user);

        String json = objectMapper.writeValueAsString(
                Login.builder()
                        .email("baejeu@naver.com")
                        .password("1234")
                        .build());
        // expected
        mockMvc.perform(
                 post("/auth/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
                 )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 성공후 accessToken 발급")
    void test2() throws Exception{
        // given
        String json = objectMapper.writeValueAsString(
                Login.builder()
                        .email("baejeu@naver.com")
                        .password("1234")
                        .build());
        // expected
        mockMvc.perform(
                 post("/auth/login")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
                 )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", notNullValue()))
                .andDo(print());

        List<Session> result = sessionRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
        System.out.println("result = " + result);
    }
    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다 /test")
    void test3() throws Exception{
        // given
        User user = User.builder()
                       .email("baejeu12@naver.com")
                       .password("12345")
                       .build();

        Session session = user.addSession(); // 로그인
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/test")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", session.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다. /test")
    void test4() throws Exception{
        // given
        User user = User.builder()
                       .email("baejeu12@naver.com")
                       .password("12345")
                       .build();

        Session session = user.addSession(); // 로그인
        userRepository.save(user);

        // expected
        mockMvc.perform(get("/test")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", session.getAccessToken() + "sss"))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
    @Test
    @DisplayName("회원가입")
    void test5() throws Exception{
        // given
        SignUp signUp = SignUp.builder()
                        .name("jeu")
                        .email("baejeu@naver.com")
                        .password("1234")
                        .build();

        String json = objectMapper.writeValueAsString(signUp);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
}