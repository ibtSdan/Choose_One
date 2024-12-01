package com.example.choose_one.controller;

import com.example.choose_one.common.Api;
import com.example.choose_one.common.ApiPagination;
import com.example.choose_one.model.ViewResponse;
import com.example.choose_one.model.PostAllResponse;
import com.example.choose_one.model.PostRequest;
import com.example.choose_one.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor

public class PostApiController {

    private final PostService postService;

    // 글 작성
    @PostMapping("/create")
    public Api<String> create(@Valid @RequestBody PostRequest postRequest){
        return postService.create(postRequest);
    }

    // 특정 글 조회
    @GetMapping("/{postId}")
    public Api<ViewResponse> view(@PathVariable Long postId){
        return postService.view(postId);
    }

    // 모든 글 조회
    @GetMapping("/all")
    public Api<ApiPagination<List<PostAllResponse>>> all(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return postService.all(pageable);
    }

    // 특정 유저 글 조회
    @GetMapping("/view/{userId}")
    public Api<ApiPagination<List<PostAllResponse>>> userPost(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 5, sort = "id",direction = Sort.Direction.DESC)Pageable pageable){
        return postService.userPost(userId,pageable);
    }
}
