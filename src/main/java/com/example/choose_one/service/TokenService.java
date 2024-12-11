package com.example.choose_one.service;

import com.example.choose_one.common.error.TokenErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.common.helper.TokenHelper;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenHelper tokenHelper;
    private final TokenRepository tokenRepository;

    public TokenDto issueAccessToken(Long userId){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        return tokenHelper.issueAccessToken(map);
    }

    public TokenDto issueRefreshToken(Long userId){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        return tokenHelper.issueRefreshToken(map);
    }

    public Long validationToken(String token){
        var map = tokenHelper.validationToken(token);
        return Long.parseLong(map.get("userId").toString());
    }

    public TokenDto reissueAccessToken(String refreshToken){
        // refreshToken으로 accessToken 재발급
        tokenHelper.validationToken(refreshToken);
        var entity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new ApiException(TokenErrorCode.TOKEN_ERROR));
        var userId = entity.getUserId();
        return issueAccessToken(userId);
    }
}
