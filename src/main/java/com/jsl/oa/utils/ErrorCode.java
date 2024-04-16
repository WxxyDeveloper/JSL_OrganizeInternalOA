package com.jsl.oa.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1>错误码</h1>
 * <hr/>
 * 用于定义错误码
 *
 * @author xiao_lfeng
 * @version v1.1.0
 * @since v1.0.0
 */
@Slf4j
@Getter
public enum ErrorCode {
    WRONG_PASSWORD("WrongPassword", 40010, "密码错误"),
    PARAMETER_ERROR("ParameterError", 40011, "参数错误"),
    REQUEST_BODY_ERROR("RequestBodyError", 40012, "请求体错误"),
    USER_EXIST("UserExist", 40013, "用户名已存在"),
    TIMESTAMP_ERROR("TimestampError", 40014, "时间戳错误"),
    USER_NOT_EXIST("UserNotExist", 40015, "用户不存在"),
    USER_ROLE_NOT_EXIST("UserRoleNotExist", 40016, "用户角色不存在"),
    USER_ROLE_NOT_MANAGER("UserRoleNotExist", 40017, "用户角色非管理员"),
    USER_NOT_CHANGE_TO_THEMSELVES("UserNotChangeToThemselves", 40018, "用户不能改变自己的角色"),
    NOT_PERMISSION("NotPermission", 40019, "没有权限"),
    UNAUTHORIZED("Unauthorized", 40100, "未授权"),
    TOKEN_EXPIRED("TokenExpired", 40101, "Token已过期"),
    VERIFICATION_INVALID("VerificationInvalid", 40102, "验证码无效"),
    TOKEN_NOT_EXIST("TokenNotExist", 40103, "Token不存在"),
    CLASS_COPY_EXCEPTION("ClassCopyException", 40104, "实体类拷贝异常"),
    USER_IS_LOCKED("UserIsLocked", 40300, "用户已被锁定"),
    USER_IS_DEACTIVATED("UserIsDeactivated", 40301, "用户已被禁用"),
    NOT_ADMIN("NotAdmin", 40302, "不是管理员"),
    EMAIL_LOGIN_NOT_SUPPORT("EmailLoginNotSupport", 40303, "请使用邮箱登陆"),
    PASSWORD_NOT_SAME("PasswordNotSame", 40304, "两次密码不一致"),
    PLEASE_ASSIGN_ROLE_TO_USER("PleaseAssignRoleToUser", 40305, "请为用户添加角色"),
    USER_ALREADY_DELETE("UserAlreadyDelete", 40306, "用户已被删除"),
    USER_DISABLED("UserDisabled", 40307, "用户已被禁用"),
    USER_LOCKED("UserLocked", 40308, "用户已被锁定"),
    USER_EXPIRED("UserExpired", 40309, "用户已过期"),
    ID_NOT_EXIST("IdNotExist", 40400, "ID不存在"),
    ROLE_NOT_FOUNDED("RoleNotFounded", 40401, "角色不存在"),
    ROLE_NAME_REPEAT("RoleNameRepeat", 40402, "角色名称重复"),
    MESSAGE_ONLY_DELETE_BY_THEMSELVES("MessageOnlyDeleteByThenSelves", 40500, "用户只能删除自己的消息"),
    PERMISSION_NOT_EXIST("permissionNotExist", 40501, "权限不存在"),
    DATABASE_INSERT_ERROR("DatabaseInsertError", 50010, "数据库插入错误"),
    DATABASE_UPDATE_ERROR("DatabaseUpdateError", 50011, "数据库更新错误"),
    DATABASE_DELETE_ERROR("DatabaseDeleteError", 50012, "数据库删除错误"),
    PROJECT_NOT_EXIST("ProjectNotExist", 40016, "项目不存在"),
    PROJECT_CUTTING_NOT_EXIST("ProjectCuttingNotExist", 40017, "项目分割模块不存在"),
    PROJECT_USER_NOT_EXIST("ProjectUserNotExist", 40018, "用户项目表无对应记录"),
    PROJECT_FILE_JSON_ERROR("ProjectFileJsonError", 40019, "项目文件json格式错误"),
    PROJECT_NOT_USER("ProjectNotUser", 40020, "项目无此用户"),
    REVIEW_NOT_EXIST("ReviewNotExit", 40101, "未找到对应审核信息");


    private final String output;
    private final Integer code;
    private final String message;

    ErrorCode(String output, Integer code, String message) {
        this.output = output;
        this.code = code;
        this.message = message;
    }
}
