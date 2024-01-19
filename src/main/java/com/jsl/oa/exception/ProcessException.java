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
    public ResponseEntity<BaseResponse> businessMissingServletRequestParameterExceptionException(MissingServletRequestParameterException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(400)
                .body(ResultUtil.error(ErrorCode.PARAMETER_ERROR, e.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> businessException(@NotNull Exception e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 500, "服务器内部错误");
    }
}
