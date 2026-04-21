package com.example.choose_one.service;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.UserErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.UserEntity;
import com.example.choose_one.model.customuser.CustomUserDetails;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.model.token.TokenResponse;
import com.example.choose_one.model.user.LoginRequest;
import com.example.choose_one.model.user.SignUpRequest;
import com.example.choose_one.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    class 회원가입_테스트 {
        @Test
        void 회원가입_실패_이미존재하는_아이디() {
            var request = new SignUpRequest("example", "1234");
            when(userRepository.existsByUserId("example")).thenReturn(true);
            assertThrows(ApiException.class, () -> userService.signUp(request));
        }
    }

    @Nested
    class 로그인_테스트 {
        @Test
        void 로그인_성공() {
            var request = new LoginRequest("example", "1234");
            var userEntity = UserEntity.builder()
                    .id(1L)
                    .userId("example")
                    .password("1234")
                    .role("ROLE_USER")
                    .build();
            var userDetails = new CustomUserDetails(
                    userEntity.getId(), userEntity.getUserId(), userEntity.getPassword(), userEntity.getRole()
            );
            var authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            when(authenticationManager.authenticate(any()))
                    .thenReturn(authentication);
            when(tokenService.issueAccessToken(any(), any()))
                    .thenReturn(new TokenDto("accessToken", LocalDateTime.now()));
            when(tokenService.issueRefreshToken(any(), any()))
                    .thenReturn(new TokenDto("refreshToken", LocalDateTime.now()));

            var result = userService.login(request);
            assertNotNull(result);
            assertEquals("accessToken", result.getData().getAccessToken());
            assertEquals("refreshToken", result.getData().getRefreshToken());

            verify(tokenService).issueAccessToken(any(), any());
            verify(tokenService).issueRefreshToken(any(), any());
        }

        @Test
        void 로그인_실패_아이디_틀림() {
            var request = new LoginRequest("wrong", "1234");
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new ApiException(UserErrorCode.USER_NOT_FOUND));
            assertThrows(ApiException.class, () -> userService.login(request));
        }

        @Test
        void 로그인_실패_비밀번호_틀림() {
            var request = new LoginRequest("example", "wrong");
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("비밀번호 틀림"));
            assertThrows(BadCredentialsException.class, () -> userService.login(request));
        }
    }
}
