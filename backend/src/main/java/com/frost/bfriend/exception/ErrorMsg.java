package com.frost.bfriend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ErrorMsg {
    private String code;
    private String message;
}
