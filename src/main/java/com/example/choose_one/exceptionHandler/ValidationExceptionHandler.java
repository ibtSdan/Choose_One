package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.api.Api;
import com.example.choose_one.common.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
@Order(1)

public class ValidationExceptionHandler {
    @ExceptionHandler(value = { MethodArgumentNotValidException.class})
    public ResponseEntity<Api> validationException(MethodArgumentNotValidException e){
        log.error(""+e);
        var errorList = e.getFieldErrors().stream()
                .map(it -> {
                    var format = "%s : %s는 %s";
                    return String.format(format,it.getField(),it.getRejectedValue(),it.getDefaultMessage());
                }).toList();


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Api.ERROR(ErrorCode.BAD_REQUEST,String.join(", ",errorList)));

    }
}
