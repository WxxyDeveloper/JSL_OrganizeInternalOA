package com.jsl.oa.model.voData;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RoleRemoveUserVO {
    @NotNull(message = "用户id不能为空")
    private Long uid;
}
