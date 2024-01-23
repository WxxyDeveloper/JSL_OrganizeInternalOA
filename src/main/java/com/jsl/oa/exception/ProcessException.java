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

/**
 * <h1>异常处理</h1>
 * <hr/>
 * 用于处理异常
 *
 * @version v1.1.0
 * @since v1.1.0
 * @see HttpRequestMethodNotSupportedException
 * @see DuplicateKeyException
 * @see HttpMessageNotReadableException
 * @see MissingServletRequestParameterException
 * @see Exception
 * @see ClassCopyException
 * @see MethodArgumentTypeMismatchException
 * @author xiao_lfeng
 */
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
        return ResponseEntity
                .status(400)
                .body(ResultUtil.error(ErrorCode.PARAMETER_ERROR, "缺少 " + e.getParameterName() + " 参数"));
    }

    /**
     * <h2>业务异常</h2>
     * <hr/>
     * 用于处理业务异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> businessException(@NotNull Exception e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50000, "服务器内部错误");
    }

    /**
     * <h2>类拷贝异常</h2>
     * <hr/>
     * 用于处理类拷贝异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(value = ClassCopyException.class)
    public ResponseEntity<BaseResponse> businessClassCopyException(@NotNull ClassCopyException e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50001, "服务器内部错误");
    }

    /**
     * <h2>参数类型不匹配异常</h2>
     * <hr/>
     * 用于处理参数类型不匹配异常
     *
     * @param e 异常
     * @return {@link ResponseEntity}
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<BaseResponse> businessMethodArgumentTypeMismatchException(@NotNull MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage(), e);
        return ResultUtil.error("ServerInternalError", 50002, "服务器内部错误");
    }
}
