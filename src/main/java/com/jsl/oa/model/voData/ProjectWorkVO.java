package com.jsl.oa.model.voData;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ProjectWorkVO {

    private Long pid;
    @NotNull(message = "项目id不能为空")
    private Long projectId;
    @NotNull(message = "负责人id不能为空")
    private Long principalId;
    @NotNull(message = "周期不能为空")
    private Integer cycle;

    private Integer workLoad;
    @NotNull(message = "类型不能为空")
    private Integer type;
    @NotNull(message = "名字不能为空")
    private String name;
    private String description;
    private Integer isDelete;
    private Integer isFinish;
    private Integer status;
    private Timestamp beginTime;
    private Timestamp completeTime;
}
