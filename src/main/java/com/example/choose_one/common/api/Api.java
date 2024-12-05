package com.example.choose_one.common.api;

import com.example.choose_one.common.error.ErrorCodeIfs;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Api<T> {
    private Result result;

    @Valid
    private T data;



    public static <T> Api<T> OK(T data){
        return Api.<T>builder()
                .result(Result.OK())
                .data(data)
                .build();
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs){
        return Api.builder()
                .result(Result.ERROR(errorCodeIfs))
                .build();
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, Throwable tx){
        return Api.builder()
                .result(Result.ERROR(errorCodeIfs,tx))
                .build();
    }

    public static Api<Object> ERROR(ErrorCodeIfs errorCodeIfs, String description){
        return Api.builder()
                .result(Result.ERROR(errorCodeIfs,description))
                .build();
    }
}
