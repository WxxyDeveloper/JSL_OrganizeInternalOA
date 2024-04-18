package com.jsl.oa.model.vodata;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class ProjectDailyAddVO {

    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @NotBlank(message = "日报内容不能为空")
    private String content;

}


