package com.jsl.oa.model.vodata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * <h1>用户注册自定义实体类</h1>
 * <hr/>
 * 用于处理用户注册表单输入的数据
 *
 * @author 筱锋xiao_lfeng
 * @version v1.0.0
 * @since v1.1.0
 */
@Getter
@Setter
public class UserRegisterVO {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[0-9A-Za-z_]{3,40}$", message = "用户名只能为字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "家乡不能为空")
    private String address;

    @NotBlank(message = "电话不能为空")
    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "电话格式错误")
    private String phone;

    @NotBlank(message = "邮箱不能为空")
    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "邮箱格式错误")
    private String email;

    @Min(value = 0, message = "保密:0,男:1,女:2")
    @Max(value = 2, message = "保密:0,男:1,女:2")
    @NotNull(message = "性别不能为空")
    private Short sex;

    @NotNull(message = "年龄不能为空")
    private Short age;
}
