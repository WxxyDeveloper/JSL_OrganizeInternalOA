package com.jsl.oa.model.vodata;

import lombok.Data;

import java.util.List;

@Data
public class PermissionContentVo {

    private Long id;
    private String name;
    private String code;
    private Short type;
    private List<PermissionContentVo> children;

}


