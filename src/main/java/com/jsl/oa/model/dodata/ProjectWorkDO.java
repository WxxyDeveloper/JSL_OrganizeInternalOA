package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectWorkDO {
    private Long id;
    private Long pid;
    private Long projectId;
    private Long principalId;
    private Integer cycle;
    private Integer workLoad;
    private Integer type;
    private String name;
    private String description;
    private Integer isDelete;
    private Integer isFinish;
    private boolean status;
    private Timestamp beginTime;
    private Timestamp completeTime;
}
