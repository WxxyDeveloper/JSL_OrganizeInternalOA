package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectWorkSimpleVO {
    private String name;
    private Long principalId;
    private String principalUser;
    private Integer isFinish;
    private String description;
    private Long cycle;
    private Long projectId;
    private Long workLoad;
    private Long id;
    private String tags;
    private Integer status;
    private Timestamp beginTime;
    private Timestamp completeTime;
}
