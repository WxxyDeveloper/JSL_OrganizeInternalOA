package com.jsl.oa.exception.library;

/**
 * 自定义异常类
 * <hr/>
 * 用于表示用户未登录的情况。
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
public class NotLoginException extends RuntimeException {
    public NotLoginException(String message) {
        super(message);
    }
}
