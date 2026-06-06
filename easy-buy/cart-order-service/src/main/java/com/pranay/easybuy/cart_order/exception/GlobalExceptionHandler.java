package com.pranay.easybuy.cart_order.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pranay.easybuy.cart_order.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ApiErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({ BusinessRuleException.class })
    public ResponseEntity<ApiErrorResponse> businessRuleExceptionHandler(BusinessRuleException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({ ExternalServiceException.class })
    public ResponseEntity<ApiErrorResponse> externalServiceExceptionHandler(ExternalServiceException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_GATEWAY, e.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ApiErrorResponse> handleFieldValidationException(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {

            errors.putIfAbsent(error.getField(), error.getDefaultMessage());
        });

        String message = "Validation failed for one or more fields";
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI(), errors);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(HttpMessageNotReadableException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiErrorResponse> handleGenericExceptions(Exception e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI(), null);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String message, String path,
            Map<String, String> errors) {
        return new ResponseEntity<>(
                new ApiErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, errors),
                status);
    }
}
