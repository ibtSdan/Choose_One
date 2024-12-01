package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
@Order(1)
public class NoSuchElementExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class})
    public ResponseEntity<Api> noSuch(NoSuchElementException e){
        log.error(""+e);
        var errorList = List.of(e.getMessage());
        var error = Api.Error.builder()
                .errorMessage(errorList)
                .build();
        var response = Api.builder()
                .resultCode(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .resultMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                .error(error)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
