package com.jsl.oa.common.constant;

import lombok.Getter;

/**
 * <h1>业务常量</h1>
 * <hr/>
 * 业务常量
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author xiao_lfeng
 */
@Getter
public enum BusinessConstants {
    BUSINESS_LOGIN("login:", "登陆实现"),
    ALL_PERMISSION("all:", "所有权限"),
    USER("user:", "用户"),
    NONE("", "null");

    private final String value;
    private final String description;

    BusinessConstants(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
