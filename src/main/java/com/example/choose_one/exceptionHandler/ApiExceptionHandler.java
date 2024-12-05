package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Integer.MIN_VALUE)
@Slf4j

public class ApiExceptionHandler {
    @ExceptionHandler(value = { ApiException.class})
    public ResponseEntity<Api<Object>> apiException(ApiException e){
        log.error(""+e);
        return ResponseEntity.status(e.getErrorCodeIfs().getHttpStatusCode())
                .body(Api.ERROR(e.getErrorCodeIfs(),e.getErrorDescription()));
    }
}
