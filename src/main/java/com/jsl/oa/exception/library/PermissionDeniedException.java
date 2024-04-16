package com.jsl.oa.exception.library;

import lombok.Getter;

/**
 * 自定义异常类
 * <hr/>
 * 用于权限拒绝的情况
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
@Getter
public class PermissionDeniedException extends RuntimeException {
    private final String needGroup;

    public PermissionDeniedException(String message, String needGroup) {
        super(message);
        this.needGroup = needGroup;
    }
}
