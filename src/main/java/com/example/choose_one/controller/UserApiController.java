package com.example.choose_one.controller;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.model.token.TokenResponse;
import com.example.choose_one.model.user.LoginRequest;
import com.example.choose_one.model.user.SignUpRequest;
import com.example.choose_one.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j

public class UserApiController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "User 회원가입",description = "User 회원가입 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "1401",description = "이미 존재하는 id", content = @Content(mediaType = "application/json"))
    })
    public Api<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        return userService.signUp(signUpRequest);
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "User 로그인",description = "User 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "1402",description = "Password 불일치",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "1404",description = "User Not Found",content = @Content(mediaType = "application/json"))
    })
    public Api<TokenResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
