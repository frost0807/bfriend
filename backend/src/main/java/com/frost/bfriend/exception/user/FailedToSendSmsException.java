package com.frost.bfriend.exception.user;

public class FailedToSendSmsException extends RuntimeException {
    public FailedToSendSmsException(String message) {
        super(message);
    }
}
