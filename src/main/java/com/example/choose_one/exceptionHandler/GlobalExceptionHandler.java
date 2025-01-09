package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Order(value = Integer.MAX_VALUE) // 가장 마지막
@Slf4j

public class GlobalExceptionHandler {
    @ExceptionHandler(value = { Exception.class})
    public ResponseEntity<Api> globalExceptionHandler(Exception e){
        log.error(""+e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Api.ERROR(ErrorCode.SERVER_ERROR, "서버 에러 발생"));
    }
}
