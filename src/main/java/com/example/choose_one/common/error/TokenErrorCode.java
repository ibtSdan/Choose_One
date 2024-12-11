package com.example.choose_one.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TokenErrorCode implements ErrorCodeIfs{
    INVALID_TOKEN(400, 4401,"유효하지 않은 토큰"),
    EXPIRED_TOKEN(400, 4402, "만료된 토큰"),
    TOKEN_ERROR(500, 4403, "토큰 에러"),

    ;





    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
