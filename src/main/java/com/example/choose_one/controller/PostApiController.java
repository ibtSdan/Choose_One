package com.example.choose_one.controller;

import com.example.choose_one.model.PostRequest;
import com.example.choose_one.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostApiController {

    private final PostService postService;

    // 글 작성
    @PostMapping("/create")
    public String create(@Valid @RequestBody PostRequest postRequest){
        return postService.create(postRequest);
    }

    // 특정 글 조회

    // 모든 글 조회

    // 특정 유저 글 조회
}
