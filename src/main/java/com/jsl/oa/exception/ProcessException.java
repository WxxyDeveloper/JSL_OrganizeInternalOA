package com.jsl.oa.exception;

import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ResultUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

@ControllerAdvice
public class ProcessException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> businessException(@NotNull Exception e) {
        e.printStackTrace();
        return ResultUtil.error("Exception", 500, "服务器异常");
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse> businessMethodNotAllowedException() {
        return ResultUtil.error("MethodNotAllowed", 405, "请求方法错误");
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<BaseResponse> businessSQLIntegrityConstraintViolationException(@NotNull SQLIntegrityConstraintViolationException e) {
        if (Pattern.matches(".*Duplicate entry.*", e.getMessage())) {
            return ResultUtil.error("DuplicateEntry", 400, "数据重复");
        } else if (Pattern.matches(".*Cannot delete or update a parent row: a foreign key constraint fails.*", e.getMessage())) {
            return ResultUtil.error("DataAssociation", 400, "数据存在关联，无法删除");
        } else {
            return ResultUtil.error("DatabaseError", 400, "数据库异常");
        }
    }
}
