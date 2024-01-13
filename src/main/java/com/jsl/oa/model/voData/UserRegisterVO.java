package com.jsl.oa.model.voData;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <h1>用户注册自定义实体类</h1>
 * <hr/>
 * 用于处理用户注册表单输入的数据
 *
 * @author 筱锋xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Getter
public class UserRegisterVO {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{2,5}$", message = "用户名只能为字母、数字或下划线")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Pattern(regexp = "^(男|女|保密)$", message = "性别只能为男、女或保密")
    private String sex;

    @NotBlank(message = "年龄不能为空")
    private String age;

    @NotBlank(message = "单位不能为空")
    private String unit;

    @NotBlank(message = "职位/专业不能为空")
    private String filed;

    @NotBlank(message = "家乡不能为空")
    private String hometown;
}
