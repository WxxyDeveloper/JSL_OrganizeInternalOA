package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * <h1>用户登陆自定义实体类</h1>
 * <hr/>
 * 用于处理用户登陆表单输入的数据
 *
 * @author 175yunxuan
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
public class UserLoginVO {
    @Pattern(regexp = "^[0-9A-Za-z_]+$", message = "支持用户名/手机号/工号登陆")
    @NotBlank(message = "用户名不能为空")
    private String user;
    @NotBlank(message = "密码不能为空")
    private String password;
}
