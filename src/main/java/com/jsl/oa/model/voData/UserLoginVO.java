package com.jsl.oa.model.voData;

import lombok.Getter;

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
@Getter
public class UserLoginVO {
    @Pattern(regexp = "^[0-9A-Z]+$", message = "工号格式错误")
    private String userNum;
    @NotBlank(message = "密码不能为空")
    private String password;
}
