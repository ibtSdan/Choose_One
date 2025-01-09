package com.example.choose_one.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor

public enum VoteErrorCode implements ErrorCodeIfs{
    INVALID_VOTE(HttpStatus.BAD_REQUEST.value(),3402,"유효하지 않은 투표 입니다."),
    DUPLICATE_VOTE(HttpStatus.BAD_REQUEST.value(), 3401, "중복투표는 허용되지 않습니다."),
    ;


    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
