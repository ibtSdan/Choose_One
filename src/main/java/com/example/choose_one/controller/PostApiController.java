package com.example.choose_one.controller;

import com.example.choose_one.common.Api;
import com.example.choose_one.common.ApiPagination;
import com.example.choose_one.model.ViewResponse;
import com.example.choose_one.model.PostAllResponse;
import com.example.choose_one.model.PostRequest;
import com.example.choose_one.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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
    @Operation(summary = "글 작성",description = "글을 작성할 때 사용하는 API")
    @Parameters({
            @Parameter(name = "contentA",description = "A 선택지"),
            @Parameter(name = "contentB",description = "B 선택지")
    })
    public Api<String> create(@Valid @RequestBody PostRequest postRequest){
        return postService.create(postRequest);
    }

    // 특정 글 조회
    @GetMapping("/{postId}")
    @Operation(summary = "특정 글 조회",description = "특정 글을 조회할 때 사용하는 API")
    public Api<ViewResponse> view(@PathVariable Long postId){
        return postService.view(postId);
    }

    // 모든 글 조회
    @GetMapping("/all")
    @Operation(summary = "모든 글 조회",description = "모든 글을 조회할 때 사용하는 API")
    public Api<ApiPagination<List<PostAllResponse>>> all(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC)Pageable pageable
            ){
        return postService.all(pageable);
    }

    // 특정 유저 글 조회
    @GetMapping("/view/{userId}")
    @Operation(summary = "특정 유저 글 조회",description = "특정 유저가 작성한 글을 조회할 때 사용하는 API")
    public Api<ApiPagination<List<PostAllResponse>>> userPost(
            @PathVariable Long userId,
            @PageableDefault(page = 0, size = 5, sort = "id",direction = Sort.Direction.DESC)Pageable pageable){
        return postService.userPost(userId,pageable);
    }
}
