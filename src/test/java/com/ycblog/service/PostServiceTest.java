package com.ycblog.service;

import com.ycblog.domain.Post;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import com.ycblog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals("TITLES", post.getTitle());
        assertEquals("CONTENTS", post.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("MY TITLE" + i)
                                    .content("MY CONTENT" + i)
                                    .build();
                        })
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0,5, Sort.by(DESC, "id"));

        //when
        List<PostResponse> posts = postService.getList(pageable);

        //then
        assertEquals(5L, posts.size());
        assertEquals("MY TITLE30", posts.get(0).getTitle());
        assertEquals("MY TITLE26", posts.get(4).getTitle());
    }
}
