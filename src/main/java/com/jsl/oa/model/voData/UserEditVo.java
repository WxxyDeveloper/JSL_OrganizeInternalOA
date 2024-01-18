package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.*;


@Data
public class UserEditVo {

    @NotNull
    private Long id;

    private String address;

    @Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", message = "电话格式错误")
    private String phone;

    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "邮箱格式错误")
    private String email;

    @Min(value = 0, message = "保密:0,男:1,女:2")
    @Max(value = 2, message = "保密:0,男:1,女:2")
    private Short sex;

    private Short age;
    private String signature;
    private String avatar;
    private String nickname;
    private String description;

}


