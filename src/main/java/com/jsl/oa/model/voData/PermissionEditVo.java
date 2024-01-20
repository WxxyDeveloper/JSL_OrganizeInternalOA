package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class PermissionEditVo {

    @NotNull
    private Long id;
    private Long pid;
    private String name;
    private String code;
    private Short type;

}


