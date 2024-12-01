package com.example.choose_one.exceptionHandler;

import com.example.choose_one.common.Api;
import jakarta.validation.ValidationException;
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
