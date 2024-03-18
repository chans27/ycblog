package com.ycblog.controller;

import com.ycblog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class PostController {

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    // 글 등록
    // POST METHOD
    @PostMapping("/posts")
    public String post(@RequestBody PostCreate params) {
        log.info("params={}", params.toString());
        return "Hello World";
    }
}
