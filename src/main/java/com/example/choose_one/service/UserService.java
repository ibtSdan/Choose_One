package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.token.TokenResponse;
import com.example.choose_one.model.user.LoginRequest;
import com.example.choose_one.model.user.LoginResponse;
import com.example.choose_one.model.user.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public Api<String> signUp(SignUpRequest signUpRequest) {
        var user = userRepository.findByUserId(signUpRequest.getUserId());
        if(user.isPresent()){ // userId 중복 방지
            throw new ApiException(UserErrorCode.USER_ALREADY_EXISTS, "다른 id를 입력하십시오.");
        }

        var entity = UserEntity.builder()
                .userId(signUpRequest.getUserId())
                .password(signUpRequest.getPassword())
                .build();
        userRepository.save(entity);

        return Api.OK("회원가입 되었습니다.");
    }

    public Api<TokenResponse> login(LoginRequest loginRequest) {
        // 사용자 이름 검증
        // 비밀번호 일치 확인
        // 해당 user id 반환
        var entity = userRepository.findByUserId(loginRequest.getUserId())
                .map(it -> {
                    // 존재하면?
                    if(!it.getPassword().equals(loginRequest.getPassword())){
                        throw new ApiException(UserErrorCode.INVALID_PASSWORD, "올바른 비밀번호 입력");
                    }
                    return it;
                }).orElseThrow(() -> {
                    return new ApiException(UserErrorCode.USER_NOT_FOUND, "올바른 id를 입력하십시오.");
                });

        var userId = entity.getId();
        var accessToken = tokenService.issueAccessToken(userId);
        var refreshToken = tokenService.issueRefreshToken(userId);

        var response = TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();

        return Api.OK(response);
    }

    public Api<String> me() {
        var requestContext = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        var userId = requestContext.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        return Api.OK("토큰 검증 완료"+userId);
    }
}
