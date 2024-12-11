package com.example.choose_one.common.helper;

import com.example.choose_one.common.error.TokenErrorCode;
import com.example.choose_one.common.exception.ApiException;
import com.example.choose_one.entity.TokenEntity;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class TokenHelper {
    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;

    private final TokenRepository tokenRepository;


    public TokenDto issueAccessToken(Map<String, Object> data){
        // key
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        // expired
        var expiredAt = LocalDateTime.now().plusHours(accessTokenPlusHour);
        var expiredAtInstance = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());
        // 토큰 생성
        var jwtToken = Jwts.builder()
                .signWith(key)
                .claims(data)
                .expiration(expiredAtInstance)
                .compact();

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAt)
                .build();
    }

    public TokenDto issueRefreshToken(Map<String, Object> data){
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var expiredAt = LocalDateTime.now().plusHours(refreshTokenPlusHour);
        var expiredAtInstance = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        var jwtToken = Jwts.builder()
                .signWith(key)
                .expiration(expiredAtInstance)
                .compact();

        var userId = Long.parseLong(data.get("userId").toString());
        // accessToken 재발급 위해 db 저장
        if(tokenRepository.existsByUserId(userId)){
            var entity = tokenRepository.findByUserId(userId)
                    .orElseThrow(() -> new ApiException(TokenErrorCode.TOKEN_ERROR));
            entity.setRefreshToken(jwtToken);
            tokenRepository.save(entity);
        } else{
            var entity = TokenEntity.builder()
                    .refreshToken(jwtToken)
                    .userId(userId)
                    .build();
            tokenRepository.save(entity);
        }

        return TokenDto.builder()
                .token(jwtToken)
                .expiredAt(expiredAt)
                .build();
    }


    public Map<String, Object> validationToken(String token){
        var key = Keys.hmacShaKeyFor(secretKey.getBytes());
        var parser = Jwts.parser()
                .setSigningKey(key)
                .build();
        try{
            var result = parser.parseClaimsJws(token);
            return new HashMap<String, Object>(result.getPayload());
        } catch (Exception e){
            if(e instanceof SignatureException){
                throw new ApiException(TokenErrorCode.INVALID_TOKEN);
            } else if (e instanceof ExpiredJwtException){
                throw new ApiException(TokenErrorCode.EXPIRED_TOKEN);
            } else{
                throw new ApiException(TokenErrorCode.TOKEN_ERROR);
            }
        }
    }
}
