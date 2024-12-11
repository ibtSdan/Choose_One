package com.example.choose_one.controller;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.model.token.RefreshTokenRequest;
import com.example.choose_one.model.token.TokenDto;
import com.example.choose_one.model.token.TokenResponse;
import com.example.choose_one.model.user.LoginRequest;
import com.example.choose_one.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor

public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/reissue")
    @Operation(summary = "accessToken 재발급",description = "refreshToken 이용하여 accessToken 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "성공",content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4403",description = "토큰 에러",content = @Content(mediaType = "application/json")),
    })
    public Api<TokenDto> reissueAccessToken(@RequestBody RefreshTokenRequest request){
        var tokenDto = tokenService.reissueAccessToken(request.getRefreshToken());
        return Api.OK(tokenDto);
    }
}
