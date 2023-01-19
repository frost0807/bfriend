package com.frost.bfriend.exception.user;

public class DuplicatedPhoneException extends RuntimeException {
    public DuplicatedPhoneException(String message) {
        super(message);
    }
}
