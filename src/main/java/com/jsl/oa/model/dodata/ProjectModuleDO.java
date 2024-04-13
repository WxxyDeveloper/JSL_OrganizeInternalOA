package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectModuleDO {
    private Long id;
    private Long projectChildId;
    private Long principalId;
    private Integer workLoad;
    private String description;
    private String name;
    private Integer isDelete;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp completeTime;
}
