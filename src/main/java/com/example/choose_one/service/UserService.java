package com.example.choose_one.service;

import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.LoginRequest;
import com.example.choose_one.model.LoginResponse;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public String signUp(SignUpRequest signUpRequest) {
        var entity = UserEntity.builder()
                .userId(signUpRequest.getUserId())
                .password(signUpRequest.getPassword())
                .build();
        userRepository.save(entity);

        return "Sign-Up successful";

    }

    public LoginResponse login(LoginRequest loginRequest) {
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
        return LoginResponse.builder().id(entity.getId()).build();
    }
}
