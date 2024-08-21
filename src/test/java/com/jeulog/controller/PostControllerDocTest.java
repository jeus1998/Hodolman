package com.jeulog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.jeulog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    /*
    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
    		.apply(documentationConfiguration(restDocumentation))
    		.build();
    }
     */
    @Test
    @DisplayName("글 조회")
    void test1() throws Exception{
        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", post.getId())
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        RequestDocumentation.pathParameters(
                                RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                            PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                            PayloadDocumentation.fieldWithPath("title").description("제목"),
                            PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                        ));
    }
    @Test
    @DisplayName("글 등록")
    void test2() throws Exception{
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(postCreate);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        PayloadDocumentation.requestFields(
                          PayloadDocumentation.fieldWithPath("title").description("제목"),
                          PayloadDocumentation.fieldWithPath("content").description("내용")
                        )
                        ));
    }
}
