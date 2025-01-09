package com.example.choose_one.service;

import com.example.choose_one.common.error.TokenErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.common.helper.TokenHelper;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenHelper tokenHelper;
    private final TokenRepository tokenRepository;

    public TokenDto issueAccessToken(Long userId, Collection<? extends GrantedAuthority> authorities){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        var authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority).toList();

        map.put("authorities",authoritiesList);
        return tokenHelper.issueAccessToken(map);
    }

    public TokenDto issueRefreshToken(Long userId, Collection<? extends GrantedAuthority> authorities){
        var map = new HashMap<String, Object>();
        map.put("userId",userId);
        var authoritiesList = authorities.stream()
                        .map(GrantedAuthority::getAuthority).toList();
        map.put("authorities",authoritiesList);
        return tokenHelper.issueRefreshToken(map);
    }

    public Map<String, Object> validationToken(String token){
        return tokenHelper.validationToken(token);
    }

    public TokenDto reissueAccessToken(String refreshToken){
        // refreshToken으로 accessToken 재발급
        tokenHelper.validationToken(refreshToken);
        var entity = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new ApiException(TokenErrorCode.TOKEN_ERROR));
        var userId = entity.getUserId();
        var roles = entity.getRole();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String role : roles.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return issueAccessToken(userId, authorities);
    }
}
