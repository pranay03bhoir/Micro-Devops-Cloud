package com.pranay.easybuy.products.exceptions;

public class InvalidRequestException extends RuntimeException {
	public InvalidRequestException() {
	}

	public InvalidRequestException(String message) {
		super(message);
	}
}
