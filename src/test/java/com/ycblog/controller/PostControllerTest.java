package com.ycblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycblog.domain.Post;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("/post 요청시 Hello world를 출력")
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
                        //.content("{\"title\": \"제목\", \"content\": \"내용\"}")
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 요청시 title값은 필수")
        void test2() throws Exception {
            //given
            PostCreate request = PostCreate.builder()
                    .content("CONTENT")
                    .build();

            String json = objectMapper.writeValueAsString(request); // java object -> json

            //expected
            mockMvc.perform(post("/posts")
                                    .contentType(APPLICATION_JSON)
                            .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("Bad Request."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("/post 요청시 DB에 값 저장")
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
                        .content("{\"title\": \"TITLE\" , \"content\": \"CONTENT\"}")
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("TITLE", post.getTitle());
        assertEquals("CONTENT", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("CONTENTS")
                .build();
        postRepository.save(post);

        //타이틀 길이를 10글자로..
        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("CONTENTS"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        Post post1 = postRepository.save(Post.builder()
                .title("TITLE1")
                .content("CONTENTS1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("TITLE2")
                .content("CONTENTS2")
                .build());

        //expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id").value(post1.getId()))
                .andExpect(jsonPath("$[0].title").value("TITLE1"))
                .andExpect(jsonPath("$[0].content").value("CONTENTS1"))
                .andExpect(jsonPath("$[1].id").value(post2.getId()))
                .andExpect(jsonPath("$[1].title").value("TITLE2"))
                .andExpect(jsonPath("$[1].content").value("CONTENTS2"))


                .andDo(print());
    }
}