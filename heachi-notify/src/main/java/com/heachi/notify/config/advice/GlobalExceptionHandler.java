package com.heachi.notify.config.advice;

import com.heachi.admin.common.response.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public JsonResult exception(Exception e) {

        return JsonResult.failOf(e.getMessage());
    }
}
