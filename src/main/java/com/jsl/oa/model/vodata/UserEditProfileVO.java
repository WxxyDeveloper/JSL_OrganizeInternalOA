package com.jsl.oa.model.vodata;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Setter
@Getter
public class UserEditProfileVO {
    private Long id;
    @Pattern(regexp = "^[0-9A-Za-z_]{3,40}$", message = "用户名只能为字母、数字或下划线")
    private String username;
    private String address;
    private String phone;
    private String email;
    private Short age;
    private String signature;
    private String avatar;
    private String nickname;
    private Short sex;
    private String description;
}
