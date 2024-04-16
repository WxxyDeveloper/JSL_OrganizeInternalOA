package com.jsl.oa.model.vodata;

import lombok.Data;

@Data
public class PermissionContentVO {
    // 主键
    private Long id;
    // 权限名称
    private String name;
    // 权限描述
    private String description;
}

