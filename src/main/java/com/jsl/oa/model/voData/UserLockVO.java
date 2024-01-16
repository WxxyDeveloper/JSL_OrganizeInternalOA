package com.jsl.oa.model.voData;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class UserLockVO {
    @NotNull(message = "id不能为空")
    private Long id;

}
