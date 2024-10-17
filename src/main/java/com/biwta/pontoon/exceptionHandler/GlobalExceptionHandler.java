package com.biwta.pontoon.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nasimkabir
 * ৮/৮/২৩
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // form field validation
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<String>> handle(MethodArgumentNotValidException exception) {
        return Collections.singletonMap(
                "errors",
                exception
                        .getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList()));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<Map<String, Object>>> handle(ConstraintViolationException exception) {
        return Collections.singletonMap(
                "errors",
                exception
                        .getConstraintViolations()
                        .stream()
                        .map(
                                x -> {
                                    HashMap<String, Object> error = new HashMap<>();
                                    error.put("field", x.getPropertyPath().toString());
                                    error.put("error", x.getMessage());
                                    return error;
                                })
                        .collect(Collectors.toList()));
    }
}