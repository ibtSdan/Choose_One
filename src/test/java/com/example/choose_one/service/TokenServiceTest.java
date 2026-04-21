package com.example.choose_one.service;

import com.example.choose_one.common.error.TokenErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.common.helper.TokenHelper;
import com.example.choose_one.entity.TokenEntity;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private TokenHelper tokenHelper;

    @Mock
    private TokenRepository tokenRepository;

    @Test
    void accessToken_발급_성공() {
        var userId = 1L;
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(tokenHelper.issueAccessToken(any()))
                .thenReturn(new TokenDto("accessToken", LocalDateTime.now()));
        var result = tokenService.issueAccessToken(userId, authorities);
        assertEquals("accessToken", result.getToken());
        verify(tokenHelper).issueAccessToken(any());
    }

    @Test
    void refreshToken_발급_성공() {
        var userId = 1L;
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(tokenHelper.issueRefreshToken(any()))
                .thenReturn(new TokenDto("refreshToken", LocalDateTime.now()));
        var result = tokenService.issueRefreshToken(userId, authorities);
        assertEquals("refreshToken", result.getToken());
        verify(tokenHelper).issueRefreshToken(any());
    }

    @Test
    void accessToken_재발급_성공() {
        var refreshToken = "refresh";
        var entity = TokenEntity.builder()
                .id(1L)
                .userId(1L)
                .refreshToken(refreshToken)
                .role("ROLE_USER")
                .build();

        when(tokenHelper.validationToken(refreshToken))
                .thenReturn(Map.of());
        when(tokenRepository.findByRefreshToken(refreshToken))
                .thenReturn(Optional.of(entity));
        when(tokenHelper.issueAccessToken(any()))
                .thenReturn(new TokenDto("newAccess", LocalDateTime.now()));

        var result = tokenService.reissueAccessToken(refreshToken);
        assertEquals("newAccess", result.getToken());
    }

    @Test
    void accessToken_재발급_실패_validation() {
        var refreshToken = "invalid";
        when(tokenHelper.validationToken(refreshToken))
                .thenThrow(new ApiException(TokenErrorCode.INVALID_TOKEN));
        assertThrows(ApiException.class, () -> tokenService.reissueAccessToken(refreshToken));
        verify(tokenRepository, never()).findByRefreshToken(refreshToken);
    }

    @Test
    void accessToken_재발급_실패_DB에_없음() {
        var refreshToken = "empty";
        when(tokenHelper.validationToken(refreshToken))
                .thenReturn(Map.of());
        when(tokenRepository.findByRefreshToken(refreshToken))
                .thenThrow(new ApiException(TokenErrorCode.TOKEN_ERROR));
        assertThrows(ApiException.class, () -> tokenService.reissueAccessToken(refreshToken));
    }
}
