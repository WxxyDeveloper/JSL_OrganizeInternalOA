package com.jsl.oa.model.vodata;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewAddVO {

    //申请名称
    @NotBlank(message = "审核名称不能为空")
    private String name;
    //申请理由
    @NotBlank(message = "申请理由不能为空")
    private String content;
    //申请的项目id
    @NotNull(message = "项目id不能为空")
    private Long projectId;
    //申请的子系统id
    @NotNull(message = "子系统id不能为空")
    private Long projectChildId;
    //申请的子模块id
    private Long projectModuleId;

}


