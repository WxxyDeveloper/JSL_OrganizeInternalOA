package com.jsl.oa.model.voData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class ProjectEditVO {

    private String name;
    private Long principalId;
    private String tags;
    private Long cycle;
    private String file;
    private String description;
    private Integer isFinish;
    private Integer status;
    private Long workLoad;
    private Timestamp beginTime;
    private Timestamp completeTime;
    private Timestamp deadline;

}


