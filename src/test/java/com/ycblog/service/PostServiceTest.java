package com.ycblog.service;

import com.ycblog.domain.Post;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개 조회
     */

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder()
                .title("TITLES")
                .content("CONTENTS")
                .build();
        postRepository.save(requestPost);

        //when
        Post post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("TITLES", post.getTitle());
        assertEquals("CONTENTS", post.getContent());
    }
}