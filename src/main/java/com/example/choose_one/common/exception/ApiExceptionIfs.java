package com.example.choose_one.common.exception;

import com.example.choose_one.common.error.ErrorCodeIfs;

public interface ApiExceptionIfs {
    ErrorCodeIfs getErrorCodeIfs();
    String getErrorDescription();
}
