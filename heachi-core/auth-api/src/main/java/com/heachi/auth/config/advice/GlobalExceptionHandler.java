package com.heachi.auth.config.advice;

import com.heachi.admin.common.response.JsonResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public JsonResult bindException(BindException e) {
        return JsonResult.failOf(
                e.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .collect(Collectors.joining(", "))
        );
    }

    @ExceptionHandler(Exception.class)
    public JsonResult<Exception> exception(Exception e) {
        return JsonResult.failOf(e.getMessage());
    }


}
