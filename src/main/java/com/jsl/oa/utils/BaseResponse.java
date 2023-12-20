package com.jsl.oa.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private final String output;
    private final Integer code;
    private final String message;
    private final Object data;

    public BaseResponse(String output, Integer code, String message, Object data) {
        this.output = output;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(String output, Integer code, String message) {
        this.output = output;
        this.code = code;
        this.message = message;
        this.data = null;
    }
}
