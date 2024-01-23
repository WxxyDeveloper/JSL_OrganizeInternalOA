package com.jsl.oa.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

/**
 * <h1>结果工具类</h1>
 * <hr/>
 * 用于返回结果
 *
 * @version v1.1.0
 * @since v1.1.0
 * @author xiao_lfeng
 */
@Slf4j
public class ResultUtil {

    @Contract(" -> new")
    public static @NotNull BaseResponse success() {
        log.info("成功: Success[200] {}", "操作成功");
        log.info("==================================================");
        return new BaseResponse("Success", 200, "操作成功", null);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse success(String message) {
        log.info("成功: Success[200] {}", message);
        log.info("==================================================");
        return new BaseResponse("Success", 200, message, null);
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BaseResponse success(Object data) {
        log.info("成功: Success[200] {}", "操作成功");
        log.info("==================================================");
        return new BaseResponse("Success", 200, "操作成功", data);
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull BaseResponse success(String message, Object data) {
        log.info("成功: Success[200] {}", message);
        log.info("==================================================");
        return new BaseResponse("Success", 200, message, data);
    }

    @Contract("_ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode) {
        log.warn("失败: 错误码[" + errorCode.getCode() + "] {} - {}", errorCode.getOutput(), errorCode.getMessage());
        log.info("==================================================");
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage());
    }

    @Contract("_, _ -> new")
    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode, Object data) {
        log.warn("失败: 错误码[" + errorCode.getCode() + "] {} - {}", errorCode.getOutput(), errorCode.getMessage());
        log.info("==================================================");
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), data);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    public static @NotNull BaseResponse error(String output, Integer code, String message, Object data) {
        log.warn("失败: 错误码[" + code + "] {} - {}", output, message);
        log.info("==================================================");
        return new BaseResponse(output, code, message, data);
    }

    public static @NotNull ResponseEntity<BaseResponse> error(String output, Integer code, String message) {
        log.warn("失败: 错误码[" + code + "] {} - {}", output, message);
        log.info("==================================================");
        return ResponseEntity.status(500)
                .body(new BaseResponse(output, code, message));
    }
}
