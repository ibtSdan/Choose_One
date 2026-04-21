package com.example.choose_one.Helper;

import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.common.helper.TokenHelper;
import com.example.choose_one.entity.TokenEntity;
import com.example.choose_one.repository.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenHelperTest {

    @InjectMocks
    private TokenHelper tokenHelper;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenHelper, "secretKey", "asdfsdfjierjhfglkdjfvoihdoigvhjerdfhgolerg");
        ReflectionTestUtils.setField(tokenHelper, "accessTokenPlusHour", 1L);
        ReflectionTestUtils.setField(tokenHelper, "refreshTokenPlusHour", 1L);
    }

    @Test
    void accessToken_생성() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1L);
        data.put("authorities", List.of("ROLE_USER"));

        var tokenDto = tokenHelper.issueAccessToken(data);

        assertNotNull(tokenDto.getToken());
        var result = tokenHelper.validationToken(tokenDto.getToken());
        assertEquals("1", result.get("userId").toString());
    }

    @Test
    void refreshToken_신규_저장() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1L);
        data.put("authorities", List.of("ROLE_USER"));

        when(tokenRepository.existsByUserId(1L))
                .thenReturn(false);

        var tokenDto = tokenHelper.issueRefreshToken(data);

        assertNotNull(tokenDto.getToken());
        verify(tokenRepository).save(any(TokenEntity.class));
    }

    @Test
    void refreshToken_기존유저_업데이트() {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", 1L);
        data.put("authorities", List.of("ROLE_USER"));

        when(tokenRepository.existsByUserId(1L))
                .thenReturn(true);
        var entity = TokenEntity.builder()
                .id(1L)
                .refreshToken("old")
                .userId(1L)
                .role("ROLE_USER")
                .build();
        when(tokenRepository.findByUserId(1L))
                .thenReturn(Optional.of(entity));

        tokenHelper.issueRefreshToken(data);
        verify(tokenRepository).save(entity);
    }

    @Test
    void validationToken_실패() {
        var token = "invalid";
        assertThrows(ApiException.class, () -> tokenHelper.validationToken(token));
    }
}
