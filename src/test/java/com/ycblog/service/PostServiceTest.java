package com.ycblog.service;

import com.ycblog.domain.Post;
import com.ycblog.exception.PostNotFound;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import com.ycblog.request.PostEdit;
import com.ycblog.request.PostSearch;
import com.ycblog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("write")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        //when
        postService.write(postCreate);
        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("TITLE", post.getTitle());
        assertEquals("CONTENT", post.getContent());
    }

    @Test
    @DisplayName("find one post")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("TITLES")
                .content("CONTENTS")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("TITLES", post.getTitle());
        assertEquals("CONTENTS", post.getContent());
    }

    @Test
    @DisplayName("find multiple posts")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("MY TITLE" + i)
                            .content("MY CONTENT" + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("MY TITLE19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("Post Title Update")
    void test4() {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("NEW_TITLE")
                .content("CONTENT")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("This post does not exist. id=" + post.getId()));
        assertEquals("NEW_TITLE", changedPost.getTitle());
        assertEquals("CONTENT", changedPost.getContent());
    }

    @Test
    @DisplayName("Post Content Update")
    void test5() {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("NEW_CONTENT")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("This post does not exist. id=" + post.getId()));
        assertEquals("TITLE", changedPost.getTitle());
        assertEquals("NEW_CONTENT", changedPost.getContent());
    }

    @Test
    @DisplayName("Post Content Update")
    void test6() {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("NEW_CONTENT")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("This post does not exist. id=" + post.getId()));
        assertEquals("TITLE", changedPost.getTitle());
        assertEquals("NEW_CONTENT", changedPost.getContent());
    }

    @Test
    @DisplayName("delete post")
    void test7() {
        // given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        // when
        postService.delete(post.getId());
        // then
        assertEquals(postRepository.count(), 0);
    }

    @Test
    @DisplayName("find - not exist post")
    void test8() {
        // given
        Post post = Post.builder()
                .title("YEACHAN")
                .content("CON")
                .build();
        postRepository.save(post);

        // expected
        //error msg validation
        Assertions.assertThrows(PostNotFound.class, () -> {
          postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("delete - not exist post")
    void test9() {
        // given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("update - post not exist")
    void test10() {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("NEW_CONTENT")
                .build();
        // expected
        Assertions.assertThrows(PostNotFound.class, () -> {
           postService.edit(post.getId() + 1, postEdit);
        });
    }

}
