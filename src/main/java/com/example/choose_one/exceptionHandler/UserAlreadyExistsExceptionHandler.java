package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.Api;
import com.example.choose_one.exceptionHandler.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
@Order(1)
public class UserAlreadyExistsExceptionHandler {
    @ExceptionHandler(value = { UserAlreadyExistsException.class})
    public ResponseEntity<Api> userAlready(UserAlreadyExistsException e){
        log.error(""+e);
        var errorList = List.of(e.getMessage());
        var error = Api.Error.builder()
                .errorMessage(errorList)
                .build();
        var response = Api.builder()
                .resultCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .resultMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .error(error)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
