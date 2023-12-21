package com.jsl.oa.exception;

import com.jsl.oa.utils.ErrorCode;

/**
 * <h1>业务异常类</h1>
 * <hr/>
 * 用于处理业务异常
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author 筱锋xiao_lfeng
 * @see RuntimeException
 */
public class BusinessException extends RuntimeException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getOutput() + "|" + errorCode.getMessage());
    }
}
