package com.example.choose_one.service;

import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.SignUpRequest;
import com.example.choose_one.model.SignUpResponse;
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
}
