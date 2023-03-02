package com.frost.bfriend.exception.matchpost;

public class ForbiddenReplyException extends RuntimeException {
    public ForbiddenReplyException(String message) {
        super(message);
    }
}
