package com.jsl.oa.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ResultUtil {

    @Contract(" -> new")
    public static @NotNull BaseResponse success() {
        log.info("成功: Success[200] {}", "操作成功");
        return new BaseResponse("Success", 200, "操作成功", null);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse success(String message) {
        log.info("成功: Success[200] {}", message);
        return new BaseResponse("Success", 200, message, null);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BaseResponse success(Object data) {
        log.info("成功: Success[200] {}", "操作成功");
        return new BaseResponse("Success", 200, "操作成功", data);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull BaseResponse success(String message, Object data) {
        log.info("成功: Success[200] {}", message);
        return new BaseResponse("Success", 200, message, data);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode) {
        log.warn("失败: 错误码[" + errorCode.getCode() + "] {} - {}", errorCode.getOutput(), errorCode.getMessage());
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage());
    }

    @Contract("_, _ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode, Object data) {
        log.warn("失败: 错误码[" + errorCode.getCode() + "] {} - {}", errorCode.getOutput(), errorCode.getMessage());
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), data);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull BaseResponse error(String output, Integer code, String message, Object data) {
        log.warn("失败: 错误码[" + code + "] {} - {}", output, message);
        return new BaseResponse(output, code, message, data);
    }

    public static @NotNull ResponseEntity<BaseResponse> error(String output, Integer code, String message) {
        log.warn("失败: 错误码[" + code + "] {} - {}", output, message);
        return ResponseEntity.status(code)
                .body(new BaseResponse(output, code, message));
    }
}
