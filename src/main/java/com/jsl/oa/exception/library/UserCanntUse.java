package com.jsl.oa.exception.library;

/**
 * 自定义异常类
 * <hr/>
 * 用于反馈用户被禁止使用的情况
 *
 * @since v1.2.0
 * @version v1.2.0
 * @author xiao_lfeng
 */
public class UserCanntUse extends RuntimeException {
    public UserCanntUse(String message) {
        super(message);
    }
}
