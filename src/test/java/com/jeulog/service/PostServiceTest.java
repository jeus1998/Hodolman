package com.jeulog.service;

import com.jeulog.domain.Post;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import com.jeulog.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
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
    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        // given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        // when
        PostResponse response = postService.get(post.getId());

        // then
        assertThat(response.getTitle()).isEqualTo(post.getTitle());
    }
    @Test
    @DisplayName("글 1페이지 조회")
    void test3(){
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

        // when
        List<PostResponse> posts = postService.getList(0);

        // then
        assertThat(posts.size()).isEqualTo(5);
        assertThat(posts).extracting("title")
                .containsExactly("호돌맨 제목 30", "호돌맨 제목 29", "호돌맨 제목 28", "호돌맨 제목 27", "호돌맨 제목 26");
    }
}