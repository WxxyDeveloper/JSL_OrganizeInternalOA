package com.jsl.oa.model.voData;

import lombok.Data;
import java.util.List;

@Data
public class PermissionContentVo {

    private Long id;
    private Long pid;
    private String name;
    private String code;
    private Short type;
    private List<PermissionContentVo> children;

}


