package com.example.choose_one.controller;

import com.example.choose_one.model.LoginRequest;
import com.example.choose_one.model.LoginResponse;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.service.UserService;
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
    public String signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        return userService.signUp(signUpRequest);
    }

    // 로그인
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }
}
