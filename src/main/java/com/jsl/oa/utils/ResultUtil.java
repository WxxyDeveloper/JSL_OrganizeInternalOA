package com.jsl.oa.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResultUtil {

    @Contract(" -> new")
    public static @NotNull BaseResponse success() {
        log.info("请求接口成功[200] 不含数据");
        return new BaseResponse("Success", 200, "操作成功", null);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse success(String message) {
        log.info(message + "[200]");
        return new BaseResponse("Success", 200, message, null);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BaseResponse success(Object data) {
        log.info("请求接口成功[200] 带数据");
        return new BaseResponse("Success", 200, "操作成功", data);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull BaseResponse success(String message, Object data) {
        log.info(message + "[200] 带数据");
        return new BaseResponse("Success", 200, message, data);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode) {
        log.info("请求接口错误[" + errorCode.getCode() + "] " + errorCode.getMessage());
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage());
    }

    @Contract("_, _ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode, Object data) {
        log.info("请求接口错误[" + errorCode.getCode() + "] " + errorCode.getMessage());
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), data);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull BaseResponse error(String output, Integer code, String message, Object data) {
        log.info("请求接口错误[" + code + "] " + message);
        return new BaseResponse(output, code, message, data);
    }

    public static @NotNull ResponseEntity<BaseResponse> error(String output, Integer code, String message) {
        log.info("请求接口错误[" + code + "] " + message);
        return ResponseEntity.status(code)
                .body(new BaseResponse(output, code, message));
    }
}
