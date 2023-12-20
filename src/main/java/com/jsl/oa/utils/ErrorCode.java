package com.jsl.oa.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRONG_PASSWORD("WrongPassword", 40010, "密码错误");

    private final String output;
    private final Integer code;
    private final String message;
    ErrorCode(String output, Integer code, String message) {
        this.output = output;
        this.code = code;
        this.message = message;
    }
}
