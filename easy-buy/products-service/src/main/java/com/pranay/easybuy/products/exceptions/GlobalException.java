package com.pranay.easybuy.products.exceptions;

import com.pranay.easybuy.products.utils.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
		return new ResponseEntity<>(new APIResponse(e.getMessage(), HttpStatus.NOT_FOUND, false), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = InvalidRequestException.class)
	public ResponseEntity<APIResponse> invalidRequestExceptionHandler(InvalidRequestException e) {
		return new ResponseEntity<>(new APIResponse(e.getMessage(), HttpStatus.BAD_REQUEST, false),
				HttpStatus.BAD_REQUEST);
	}
}
