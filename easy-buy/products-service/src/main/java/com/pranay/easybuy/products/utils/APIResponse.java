package com.pranay.easybuy.products.utils;

import org.springframework.http.HttpStatus;

public class APIResponse {
    String message;
    HttpStatus statusCode;
    Boolean status;

    public APIResponse(String message, HttpStatus statusCode, Boolean status) {
        this.message = message;
        this.statusCode = statusCode;
        this.status = status;
    }
}
