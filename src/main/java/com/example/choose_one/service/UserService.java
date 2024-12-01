package com.example.choose_one.service;

import com.example.choose_one.common.Api;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.exceptionHandler.exception.InvalidPasswordException;
import com.example.choose_one.exceptionHandler.exception.UserAlreadyExistsException;
import com.example.choose_one.model.LoginRequest;
import com.example.choose_one.model.LoginResponse;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;

    public Api<String> signUp(SignUpRequest signUpRequest) {
        var user = userRepository.findByUserId(signUpRequest.getUserId());
        if(user.isPresent()){ // userId 중복 방지
            throw new UserAlreadyExistsException("이미 존재하는 User 입니다.");
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
                        throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
                    }
                    return it;
                }).orElseThrow(() -> {
                    return new NoSuchElementException("해당하는 User가 존재하지 않습니다.");
                });
        return Api.<LoginResponse>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.getReasonPhrase())
                .data(LoginResponse.builder().id(entity.getId()).build())
                .build();
    }
}
