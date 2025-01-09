package com.example.choose_one.common.exception;

import com.example.choose_one.common.error.ErrorCodeIfs;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException implements ApiExceptionIfs{
    private final ErrorCodeIfs errorCodeIfs;
    private final String errorDescription;

    public ApiException(ErrorCodeIfs errorCodeIfs){
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }
    public ApiException(ErrorCodeIfs errorCodeIfs, String description) {
        super(description);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = description;
    }
    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable tx){
        super(tx);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = errorCodeIfs.getDescription();
    }
    public ApiException(ErrorCodeIfs errorCodeIfs, Throwable tx, String description){
        super(description);
        this.errorCodeIfs = errorCodeIfs;
        this.errorDescription = description;
    }
}