package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserForgetPasswordVO {
    @NotBlank(message = "邮箱不能为空")
    private String email;
    @NotNull(message = "验证码不能为空")
    private Integer check;
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
