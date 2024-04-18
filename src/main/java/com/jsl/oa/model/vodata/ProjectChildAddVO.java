package com.jsl.oa.model.vodata;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ProjectChildAddVO {

    @NotNull(message = "项目id不能为空")
    private Long projectId;
    private Long principalId;
    @NotNull(message = "周期不能为空")
    private Integer cycle;
    @NotNull(message = "工作量不能为空")
    private Integer workLoad;
    @NotNull(message = "名字不能为空")
    private String name;
    private String description;
    private String status;
    @NotNull(message = "截止时间不能为空")
    private Timestamp deadLine;

}

