package com.jsl.oa.exception;

import com.jsl.oa.utils.ErrorCode;
import org.jetbrains.annotations.NotNull;

public class ClassCopyException extends IllegalAccessException {
    public ClassCopyException(@NotNull ErrorCode errorCode) {
        super(errorCode.getOutput() + "|" + errorCode.getMessage());
    }
}
