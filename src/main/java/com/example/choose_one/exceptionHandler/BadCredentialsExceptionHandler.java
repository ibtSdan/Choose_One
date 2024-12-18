package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(1)
public class BadCredentialsExceptionHandler {

    @ExceptionHandler(value = { BadCredentialsException.class})
    public ResponseEntity<Api> validationException(BadCredentialsException e){
        log.error(""+e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Api.ERROR(ErrorCode.BAD_REQUEST,"잘못된 비밀번호")
        );

    }
}