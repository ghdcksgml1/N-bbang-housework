package com.heachi.auth.config.advice;

import com.heachi.admin.common.response.JsonResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public JsonResult<Exception> exception(Exception e) {
        return JsonResult.failOf(e.getMessage());
    }
}
