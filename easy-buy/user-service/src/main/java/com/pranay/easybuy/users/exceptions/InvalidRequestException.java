package com.pranay.easybuy.users.exceptions;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super(message);
    }

}
