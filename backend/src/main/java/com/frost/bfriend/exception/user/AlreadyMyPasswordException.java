package com.frost.bfriend.exception.user;

public class AlreadyMyPasswordException extends RuntimeException {
    public AlreadyMyPasswordException(String message) {
        super(message);
    }
}
