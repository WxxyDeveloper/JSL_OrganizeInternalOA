package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectSimpleVO {
    private String name;
    private String principalUser;
    private Integer isFinish;
    private String description;
    private Long cycle;
    private Long workLoad;
    private Long id;
    private String tags;
}
