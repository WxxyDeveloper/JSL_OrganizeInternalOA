package com.jsl.oa.model.vodata.business.info;

import lombok.Data;

@Data
public class ProjectShowVO {
    private Integer displayOrder;
    private String name;
    private Long type;
    private Long status;
    private Boolean isActive;
}
