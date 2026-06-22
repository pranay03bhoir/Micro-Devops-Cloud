package com.pranay.easybuy.payment.exceptions;

public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException() {
    }

    public BusinessRuleException(String message) {
        super(message);
    }
}
