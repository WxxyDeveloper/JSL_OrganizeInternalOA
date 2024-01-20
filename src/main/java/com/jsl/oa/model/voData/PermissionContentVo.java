package com.jsl.oa.model.voData;

import com.fasterxml.jackson.annotation.JsonInclude;
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


