package com.jsl.oa.model.vodata;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProjectCuttingEditVO {

    @NotNull(message = "id不能为空")
    private Long id;
    @NotNull(message = "父id不能为空")
    private Long pid;
    @NotBlank(message = "模块名称不为空")
    private String name;
    private String tag;
    @NotNull(message = "工程量计算不能为空")
    private Short engineering;
    @NotNull(message = "预估时间不能为空")
    private Integer estimatedTime;

    private Integer realTime;


}


