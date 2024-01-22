package com.jsl.oa.exception;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
public class ProcessException {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> businessMethodNotAllowedException() {
        log.warn("请求方法错误");
        return ResultUtil.error("MethodNotAllowed", 405, "请求方法错误");
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<BaseResponse> businessDuplicateKeyException(@NotNull DuplicateKeyException e) {
        log.warn(e.getMessage(), e);
        return ResultUtil.error("DuplicateEntry", 400, "数据重复/外键约束");
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse> businessHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(e.getMessage(), e);
        return ResultUtil.error("HttpMessageNotReadable", 400, "请求参数错误");
    }
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse> businessMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        // 使用正则表达式匹配并提取'id'部分
        Pattern pattern = Pattern.compile("'.*?'");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(e.getMessage()));

        // 查找匹配项
        while (matcher.find()) {
            String matchedGroup = matcher.group();
        }

        return ResponseEntity
                .status(400)
                .body(ResultUtil.error(ErrorCode.PARAMETER_ERROR, "缺少 " + e.getParameterName() + " 参数"));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> businessException(@NotNull Exception e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50000, "服务器内部错误");
    }

    @ExceptionHandler(value = ClassCopyException.class)
    public ResponseEntity<BaseResponse> businessClassCopyException(@NotNull ClassCopyException e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50001, "服务器内部错误");
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> businessMethodArgumentTypeMismatchException(@NotNull MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50002, "服务器内部错误");
    }
}
