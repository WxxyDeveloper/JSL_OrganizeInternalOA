package com.jsl.oa.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRONG_PASSWORD("WrongPassword", 40010, "密码错误"),
    PARAMETER_ERROR("ParameterError", 40011, "参数错误"),
    USERNAME_EXIST("UsernameExist", 40012, "用户名已存在"),
    TIMESTAMP_ERROR("TimestampError", 40013, "时间戳错误"),
    DATABASE_INSERT_ERROR("DatabaseInsertError", 50010, "数据库插入错误"),
    DATABASE_UPDATE_ERROR("DatabaseUpdateError", 50011, "数据库更新错误"),
    DATABASE_DELETE_ERROR("DatabaseDeleteError", 50012, "数据库删除错误");

    private final String output;
    private final Integer code;
    private final String message;

    ErrorCode(String output, Integer code, String message) {
        this.output = output;
        this.code = code;
        this.message = message;
    }
}
