package com.jsl.oa.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * <h1>结果工具类</h1>
 * <hr/>
 * 用于返回结果
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
public class ResultUtil {

    public static @NotNull BaseResponse success() {
        log.info("成功: Success[200] 操作成功 - 不带数据");
        log.info("==================================================");
        return new BaseResponse("Success", 200, "操作成功", null);
    }

    public static @NotNull BaseResponse success(String message) {
        log.info("成功: Success[200] {} - 不带数据", message);
        return new BaseResponse("Success", 200, message, null);
    }

    public static @NotNull BaseResponse success(Object data) {
        log.info("成功: Success[200] 操作成功 - 带数据");
        return new BaseResponse("Success", 200, "操作成功", data);
    }

    public static @NotNull BaseResponse success(String message, Object data) {
        log.info("成功: Success[200] {} - 带数据", message);
        return new BaseResponse("Success", 200, message, data);
    }

    public static @NotNull BaseResponse error(@NotNull String errorMessage, @NotNull ErrorCode errorCode) {
        log.warn("失败: 错误码[" + errorCode.getCode() + "] {} - {} - {}",
                errorCode.getOutput(),
                errorCode.getMessage(),
                errorMessage
        );
        HashMap<String, String> map = new HashMap<>();
        map.put("errorMessage", errorMessage);
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), map);
    }

    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode) {
        logBack(errorCode.getCode(), errorCode.getOutput(), errorCode.getMessage(), null);
        HashMap<String, String> map = new HashMap<>();
        map.put("errorMessage", errorCode.getMessage());
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), map);
    }

    public static @NotNull BaseResponse error(@NotNull ErrorCode errorCode, Object data) {
        logBack(errorCode.getCode(), errorCode.getOutput(), errorCode.getMessage(), data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errorMessage", errorCode.getMessage());
        map.put("errorData", data);
        return new BaseResponse(errorCode.getOutput(), errorCode.getCode(), errorCode.getMessage(), map);
    }

    public static @NotNull BaseResponse error(String output, Integer code, String message, Object data) {
        logBack(code, output, message, data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errorMessage", message);
        map.put("errorData", data);
        return new BaseResponse(output, code, message, map);
    }

    public static @NotNull ResponseEntity<BaseResponse> error(String output, Integer code, String message) {
        logBack(code, output, message, null);
        HashMap<String, Object> map = new HashMap<>();
        map.put("errorMessage", message);
        return ResponseEntity.status(500)
                .body(new BaseResponse(output, code, message, map));
    }

    /**
     * <h1>日志记录</h1>
     * <hr/>
     * 用户返回错误相关的日志内容
     *
     * @param code    错误码
     * @param output  英文输出状态信息
     * @param message 中文解释消息
     * @param data    是否有数据
     */
    private static void logBack(Integer code, String output, String message, Object data) {
        if (data != null) {
            log.warn("失败: 错误码[{}] {} - {} - 带数据", code, output, message);
        } else {
            log.warn("失败: 错误码[{}] {} - {} - 不带数据", code, output, message);
        }
    }
}
