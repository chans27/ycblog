package com.ycblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycblog.domain.Post;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import com.ycblog.request.PostEdit;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();

    }

    @Test
    @DisplayName("print hello world")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();

        String json = objectMapper.writeValueAsString(request); // java object -> json

        System.out.println("json : " + json);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("title required")
        void test2() throws Exception {
            //given
            PostCreate request = PostCreate.builder()
                    .content("CONTENT")
                    .build();

            String json = objectMapper.writeValueAsString(request); // java object -> json

            //expected
            mockMvc.perform(post("/posts")
                            .contentType(APPLICATION_JSON)
                            .content(json))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request."))
                .andExpect(jsonPath("$.validation.title").value("Title should not be blank"))
                .andDo(print());
    }

    @Test
    @DisplayName("save to DB")
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();

        String json = objectMapper.writeValueAsString(request); // java object -> json

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("TITLE", post.getTitle());
        assertEquals("CONTENT", post.getContent());
    }

    @Test
    @DisplayName("find one post")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("CONTENTS")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("CONTENTS"))
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("find multiple posts")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("MY TITLE" + i)
                            .content("MY CONTENT" + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(18).getId()))
                .andExpect(jsonPath("$[0].title").value("MY TITLE19"))
                .andExpect(jsonPath("$[0].content").value("MY CONTENT19"))
                .andDo(print());

        postRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("get first page even if set page as 0")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("MY TITLE" + i)
                            .content("MY CONTENT" + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(18).getId()))
                .andExpect(jsonPath("$[0].title").value("MY TITLE19"))
                .andExpect(jsonPath("$[0].content").value("MY CONTENT19"))
                .andDo(print());

        postRepository.deleteAll();
    }

    @Transactional
    @Test
    @DisplayName("find multiple posts")
    void test7() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("MY TITLE" + i)
                            .content("MY CONTENT" + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(requestPosts.get(18).getId()))
                .andExpect(jsonPath("$[0].title").value("MY TITLE19"))
                .andExpect(jsonPath("$[0].content").value("MY CONTENT19"))
                .andDo(print());
    }

    @Test
    @DisplayName("Post Data Update")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Post Delete")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("find - not exist post")
    void test10() throws Exception{
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("update - not exist post")
    void test11() throws Exception{

        PostEdit postEdit = PostEdit.builder()
                .title("TITLE")
                .content("CONTENT")
                .build();

        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("'java' could not be included in title")
    void test12() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("java Spring")
                .content("CONTENT")
                .build();

        String json = objectMapper.writeValueAsString(request); // java object -> json

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}