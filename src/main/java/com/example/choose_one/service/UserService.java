package com.example.choose_one.service;

import com.example.choose_one.common.Api;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.LoginRequest;
import com.example.choose_one.model.LoginResponse;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public Api<String> signUp(SignUpRequest signUpRequest) {
        var user = userRepository.findByUserId(signUpRequest.getUserId());
        if(user.isPresent()){ // userId 중복 방지
            throw new RuntimeException("User already exists");
        }

        var entity = UserEntity.builder()
                .userId(signUpRequest.getUserId())
                .password(signUpRequest.getPassword())
                .build();
        userRepository.save(entity);

        return Api.<String>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data("회원가입 성공")
                .build();
    }

    public Api<LoginResponse> login(LoginRequest loginRequest) {
        // 사용자 이름 검증
        // 비밀번호 일치 확인
        // 해당 user id 반환
        var entity = userRepository.findByUserId(loginRequest.getUserId())
                .map(it -> {
                    // 존재하면?
                    if(!it.getPassword().equals(loginRequest.getPassword())){
                        throw new RuntimeException("Not Match Password");
                    }
                    return it;
                }).orElseThrow(() -> {
                    return new RuntimeException("Not Found User");
                });
        return Api.<LoginResponse>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(LoginResponse.builder().id(entity.getId()).build())
                .build();
    }
}
