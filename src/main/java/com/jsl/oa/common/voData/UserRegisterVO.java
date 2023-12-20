package com.jsl.oa.common.voData;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <h1>用户登陆自定义实体类</h1>
 * <hr/>
 * 用于处理用户注册表单输入的数据
 *
 * @since  v1.0.0
 * @version v1.0.0
 * @author 筱锋xiao_lfeng
 */
@Getter
public class UserRegisterVO {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,5}$", message = "用户名只能为字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Pattern(regexp = "^(男|女|保密)$", message = "性别只能为男或女")
    @NotBlank(message = "性别不能为空")
    private String sex;

    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    @NotBlank(message = "年龄不能为空")
    private String age;

    @NotBlank(message = "单位不能为空")
    private String unit;

    @NotBlank(message = "职位/专业不能为空")
    private String filed;

    @NotBlank(message = "家乡不能为空")
    private String hometown;
}
