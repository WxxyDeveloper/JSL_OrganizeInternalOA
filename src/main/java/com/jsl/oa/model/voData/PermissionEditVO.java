package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PermissionEditVO {

    @NotNull
    private Long id;
    private Long pid;
    private String name;
    private String code;
    private Short type;

}


