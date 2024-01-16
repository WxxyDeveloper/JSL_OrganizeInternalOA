package com.jsl.oa.model.voData;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RoleAddUser {
    @NotNull(message = "角色id不能为空")
    private Long uid;
    @NotNull(message = "角色id不能为空")
    private int rid;
}
