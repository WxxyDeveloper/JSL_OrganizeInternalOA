package com.jsl.oa.utils;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WRONG_PASSWORD("WrongPassword", 40010, "密码错误"),
    PARAMETER_ERROR("ParameterError", 40011, "参数错误"),
    REQUEST_BODY_ERROR("RequestBodyError", 40012, "请求体错误"),
    USER_EXIST("UserExist", 40013, "用户名已存在"),
    TIMESTAMP_ERROR("TimestampError", 40014, "时间戳错误"),
    USER_NOT_EXIST("UserNotExist", 40015, "用户不存在"),
    UNAUTHORIZED("Unauthorized", 40100, "未授权"),
    TOKEN_EXPIRED("TokenExpired", 40101, "Token已过期"),
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
