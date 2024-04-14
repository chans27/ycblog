package com.ycblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ycblog.domain.Post;
import com.ycblog.repository.PostRepository;
import com.ycblog.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.ycblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("REST docs - find one post")
    void test1() throws Exception {
        //given
        Post post = Post.builder()
                .title("投稿タイトル")
                .content("内容例")
                .build();
        postRepository.save(post);

        //expected
        this.mockMvc.perform(get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        pathParameters(
                            parameterWithName("postId").description("投稿ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("投稿ID"),
                                fieldWithPath("title").description("タイトル"),
                                fieldWithPath("content").description("内容")
                        )
                ));
    }

    @Test
    @DisplayName("REST docs - post create")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("投稿タイトル2")
                .content("内容2")
                .build();

        String json = objectMapper.writeValueAsString(request); // java object -> json

        //expected
        this.mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(json)
                )

                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index",
                        requestFields(
                                fieldWithPath("title").description("投稿タイトル"),
                                fieldWithPath("content").description("内容例")
                        )
                ));
    }
}
