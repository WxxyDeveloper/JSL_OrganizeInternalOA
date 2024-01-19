package com.jsl.oa.exception;

import com.jsl.oa.utils.ErrorCode;

public class ClassCopyException extends BusinessException {
    public ClassCopyException() {
        super(ErrorCode.CLASS_COPY_EXCEPTION);
    }
}
