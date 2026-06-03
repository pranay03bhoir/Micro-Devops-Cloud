package com.pranay.easybuy.users.exceptions;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.pranay.easybuy.users.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<ApiErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                e.getMessage(),
                request.getRequestURI(),
                List.of());
    }

    @ExceptionHandler({ InvalidRequestException.class })
    public ResponseEntity<ApiErrorResponse> invalidRequestExceptionHandler(InvalidRequestException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                List.of());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ApiErrorResponse> fieldErrorHandler(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        List<String> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> this.fomatFieldErrors(error))
                .toList();
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                e.getMessage(),
                request.getRequestURI(),
                fieldErrors);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiErrorResponse> handleRequestErrors(Exception e, HttpServletRequest request) {
        String message = e instanceof ConstraintViolationException ? "Validation failed" : e.getMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI(),
                List.of());
    }

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<ApiErrorResponse> handleResponseStstusException(ResponseStatusException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        return buildErrorResponse(status,
                e.getReason() != null ? e.getReason() : e.getMessage(),
                request.getRequestURI(),
                List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error: " + ex.getMessage(),
                request.getRequestURI(),
                List.of());
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String message, String path,
            List<String> details) {

        return new ResponseEntity<>(
                new ApiErrorResponse(
                        Instant.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        path,
                        details),
                status);
    }

    private String fomatFieldErrors(FieldError fieldError) {
        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
    }
}
