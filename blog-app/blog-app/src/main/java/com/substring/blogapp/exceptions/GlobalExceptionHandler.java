package com.substring.blogapp.exceptions;

import com.substring.utils.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(APIResponse.response(e.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.name()), HttpStatus.NOT_FOUND);
    }
}
