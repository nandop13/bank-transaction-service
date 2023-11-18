package com.services.banktransactionservice.exception;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException() {
        super();
    }
    public PaymentFailedException(String message) {
        super(message);
    }
    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
