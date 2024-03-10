package com.jsl.oa.model.voData;

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

    private String file;

    private Timestamp completeTime;
    private Timestamp deadline;
    private Integer status;
    private Integer isFinish;

}
