package com.example.choose_one.controller;

import com.example.choose_one.common.Api;
import com.example.choose_one.model.LoginRequest;
import com.example.choose_one.model.LoginResponse;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j

public class UserApiController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    @Operation(summary = "User 회원가입",description = "User 회원가입 할 때 사용하는 API")
    public Api<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        return userService.signUp(signUpRequest);
    }

    // 로그인
    @PostMapping("/login")
    @Operation(summary = "User 로그인",description = "User 로그인 할 때 사용하는 API")
    public Api<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
