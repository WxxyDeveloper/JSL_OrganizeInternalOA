package com.jsl.oa.model.vodata;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class ProjectDailyAddVO {

    @NotNull(message = "项目id不能为空")
    private Long projectId;

    @NotBlank(message = "日报内容不能为空")
    private String content;

    @NotNull(message = "工作时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dailyTime;

}


