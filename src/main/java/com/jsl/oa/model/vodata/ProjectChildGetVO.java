package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;


@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectChildGetVO {
    private Long id;
    private String principalName;
    private Long projectId;
    private Integer workLoad;
    private Integer cycle;
    private String name;
    private String description;
    private Integer isDelete;
    private String status;
    private Timestamp deadLine;
}
