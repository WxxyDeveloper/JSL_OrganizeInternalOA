package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ProjectCuttingAddVO {

    @NotNull(message = "父id不能为空")
    private Long pid;
    @NotBlank(message = "模块名称不为空")
    private String name;
    private String tag;
    private Integer realTime;

}


