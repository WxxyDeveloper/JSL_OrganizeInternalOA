package com.jsl.oa.exception;

import com.jsl.oa.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

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
@Slf4j
public class BusinessException extends RuntimeException {

    public BusinessException(@NotNull ErrorCode errorCode) {
        super(errorCode.getOutput() + "|" + errorCode.getMessage());
        log.warn("业务异常: {}", errorCode.getOutput() + "|" + errorCode.getMessage());
    }
}
