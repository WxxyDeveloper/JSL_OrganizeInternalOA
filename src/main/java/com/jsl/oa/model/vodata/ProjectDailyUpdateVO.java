package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProjectDailyUpdateVO {


    @NotNull(message = "日报id不能为空")
    private Integer id;

    @NotNull(message = "日报所属项目id不能为空")
    private Integer projectId;

    @NotBlank(message = "日报内容不能为空")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String dailyTime;


}


