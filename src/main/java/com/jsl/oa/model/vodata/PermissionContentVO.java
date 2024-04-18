package com.jsl.oa.model.vodata;

import lombok.Data;

import java.util.List;

@Data
public class PermissionContentVO {

    private Long id;
    private String name;
    private String code;
    private Short type;
    private List<PermissionContentVO> children;

}


