package com.jsl.oa.model.vodata;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RoleAddVo {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "角色名只能为3-16位的字母、数字、下划线组成")
    private String name;
    @NotBlank
    private String displayName;

}


