package com.jsl.oa.common.constant;

import lombok.Getter;

/**
 *
 */
@Getter
public enum BusinessConstants {
    BUSINESS_LOGIN("login:", "登陆实现"),
    NONE("", "null");

    private final String value;
    private final String description;

    BusinessConstants(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
