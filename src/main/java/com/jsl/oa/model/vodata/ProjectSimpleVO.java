package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSimpleVO {
    private String name;
    private String principalUser;
    private String description;
    private Long cycle;
    private Long workLoad;
    private Long id;
    private String files;
    private String status;
    private Date deadLine;
    private String tags;
}
