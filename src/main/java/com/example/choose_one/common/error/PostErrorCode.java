package com.example.choose_one.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements ErrorCodeIfs{
    POST_NOT_FOUND(HttpStatus.NOT_FOUND.value(),2404,"해당하는 post가 존재하지 않습니다."),

    ;


    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
