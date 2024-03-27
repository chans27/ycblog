package com.ycblog.controller;

import com.ycblog.domain.Post;
import com.ycblog.request.PostCreate;
import com.ycblog.response.PostResponse;
import com.ycblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        // Case1. 저장한 데이터 Entity -> response로 응답
        // Case2. 저장한 데이터의 primary_id -> response로 응답
        //        Client에서는 수신한 id를 post조회 API를 통해서 글 데이터를 수신받음
        // Case3. 응답 필요없음 -> 클라이언트에서 모든 POST데이터의 context를 관리함
        // Bad Case : 서버에서 -> 반드시 이렇게 할것이라 fix해두는 케이스 (서버에서는 차라리 유연하게 대응하는게 좋다) = 코드를 잘 짜야한다.
        //
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

}
