package com.example.choose_one.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public enum UserErrorCode implements ErrorCodeIfs{
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(),1404,"해당하는 User가 존재하지 않습니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), 1401, "이미 존재하는 User 이름 입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST.value(), 1402,"Password 불일치"),

    ;





    private final Integer httpStatusCode;
    private final Integer errorCode;
    private final String description;
}
