package com.jsl.oa.model.vodata;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ProjectInfoVO {

    private Long id;
    @NotNull(message = "负责人id不能为空")
    private Long principalId;
    @NotBlank(message = "项目名不能为空")
    private String name;
    private String description;
    @NotNull(message = "周期不能为空")
    private Long cycle;
    @NotNull(message = "工作量不能为空")
    private Long workLoad;
    private String tags;
    private String files;
    private Timestamp beginTime;
    private Timestamp completeTime;
    @NotNull(message = "截止时间不能为空")
    private Timestamp deadLine;
    private String status;

}
