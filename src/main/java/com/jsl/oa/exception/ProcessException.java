package com.jsl.oa.exception;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProcessException {

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> methodNotAllowedException() {
        return ResultUtil.error("MethodNotAllowed", 405, "请求方法错误");
    }
}
