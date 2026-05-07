package com.pranay.easybuy.products.exceptions;

import com.pranay.easybuy.products.responseBuilder.APIResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e,
			HttpServletRequest rq) {
		return buildResponse(HttpStatus.NOT_FOUND, e.getMessage(), rq.getRequestURI(), List.of(), false);
	}

	@ExceptionHandler(value = InvalidRequestException.class)
	public ResponseEntity<APIResponse> invalidRequestExceptionHandler(InvalidRequestException e,
			HttpServletRequest rq) {
		return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), rq.getRequestURI(), List.of(), false);
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ResponseEntity<APIResponse> validationExceptionHandler(MethodArgumentNotValidException e,
			HttpServletRequest rq) {
		List<String> fieldErrors = e.getBindingResult().getFieldErrors().stream().map(this::formatFieldError).toList();
		return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), rq.getRequestURI(), fieldErrors, false);
	}

	@ExceptionHandler(value = { MissingServletRequestParameterException.class,
			MethodArgumentTypeMismatchException.class, ConstraintViolationException.class,
			HttpMessageNotReadableException.class, IllegalArgumentException.class })
	public ResponseEntity<APIResponse> handleCommonException(Exception e, HttpServletRequest rq) {
		String message = e instanceof ConstraintViolationException ? "Validation Failed" : e.getMessage();
		return buildResponse(HttpStatus.BAD_REQUEST, message, rq.getRequestURI(), List.of(), false);
	}

	private ResponseEntity<APIResponse> buildResponse(HttpStatus statusCode, String message, String path,
			List<String> fieldErrors, Boolean status) {
		APIResponse apiResponse = new APIResponse(Instant.now(), message, statusCode.value(), path, status,
				statusCode.getReasonPhrase(), fieldErrors);
		return new ResponseEntity<>(apiResponse, statusCode);
	}

	private String formatFieldError(FieldError fieldError) {
		return fieldError.getField() + ": " + fieldError.getDefaultMessage();
	}
}
