package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.customuser.CustomUserDetails;
import com.example.choose_one.model.token.TokenResponse;
import com.example.choose_one.model.user.LoginRequest;
import com.example.choose_one.model.user.LoginResponse;
import com.example.choose_one.model.user.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@Service
@RequiredArgsConstructor

public class UserService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Api<String> signUp(SignUpRequest signUpRequest) {
        var user = userRepository.findByUserId(signUpRequest.getUserId());
        if(user.isPresent()){ // userId 중복 방지
            throw new ApiException(UserErrorCode.USER_ALREADY_EXISTS, "다른 id를 입력하십시오.");
        }

        var encodePw = passwordEncoder.encode(signUpRequest.getPassword());
        var entity = UserEntity.builder()
                .userId(signUpRequest.getUserId())
                .password(encodePw)
                .role("ROLE_USER")
                .build();
        userRepository.save(entity);

        return Api.OK("회원가입 되었습니다.");
    }

    public Api<TokenResponse> login(LoginRequest loginRequest) {
        // 인증에 성공하면 자동으로 user 정보 갱신해줌. 그래서 UsernamePasswordAuthenticationToken 사용
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUserId(), loginRequest.getPassword()
        );

        // authenticationManager 가 호출되면, 자동으로 authenticationProvider 호출
        // Provider 에서 설정된 UserDetailsService로 해당하는 user를 db에서 가져온다
        // 가져온 user를 match로 검증
        // 성공하면 authentication 객체 반환
        var authentication = authenticationManager.authenticate(authenticationToken);

        var user = (CustomUserDetails) authentication.getPrincipal();

        var userId = user.getUserId();
        var authorities = user.getAuthorities();

        var accessToken = tokenService.issueAccessToken(userId,authorities);
        var refreshToken = tokenService.issueRefreshToken(userId,authorities);

        var response = TokenResponse.builder()
                .accessToken(accessToken.getToken())
                .accessTokenExpiredAt(accessToken.getExpiredAt())
                .refreshToken(refreshToken.getToken())
                .refreshTokenExpiredAt(refreshToken.getExpiredAt())
                .build();

        return Api.OK(response);
    }

    public Api<String> me() {
        var requestContext = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) requestContext.getPrincipal();
        return Api.OK("토큰 검증 완료: "+userId);
    }
}
