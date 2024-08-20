package com.jeulog.service;

import com.jeulog.domain.Post;
import com.jeulog.exception.PostNotFound;
import com.jeulog.repository.PostRepository;
import com.jeulog.request.PostCreate;
import com.jeulog.request.PostEdit;
import com.jeulog.request.PostSearch;
import com.jeulog.response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        PostSearch postSearch = PostSearch.builder()
                //.page(1)
                //.size(10)
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);

        // then
        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts).extracting("title")
                .contains("호돌맨 제목 30", "호돌맨 제목 29", "호돌맨 제목 28", "호돌맨 제목 27", "호돌맨 제목 26");
    }
    @Test
    @DisplayName("수정")
    void test4(){
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

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));

        assertThat(changedPost.getTitle()).isEqualTo("호돌걸");
        assertThat(changedPost.getContent()).isEqualTo("자이반포");
    }
    @Test
    @DisplayName("게시글 삭제")
    void test5(){
        // given
        Post post = Post.builder()
                  .title("호돌맨")
                  .content("반포자이")
                  .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        assertThat(postRepository.count()).isEqualTo(0);
    }
    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test6(){
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // expected
        assertThatThrownBy(() ->  postService.get(post.getId() + 1))
                .isInstanceOf(PostNotFound.class)
                .hasMessageContaining("존재하지 않는 글입니다.");
    }
    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글 삭제")
    void test7(){
        // given
        Post post = Post.builder()
                  .title("호돌맨")
                  .content("반포자이")
                  .build();
        postRepository.save(post);

        // expected
        assertThatThrownBy(()-> postService.delete(post.getId() + 1))
                .isInstanceOf(PostNotFound.class);
    }

    @Test
    @DisplayName("게시글 수정 - 존재하지 않는 글 수정")
    void test8(){
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

        // expected
        assertThatThrownBy(()-> postService.edit(post.getId() + 1, postEdit))
                     .isInstanceOf(PostNotFound.class);
    }
}