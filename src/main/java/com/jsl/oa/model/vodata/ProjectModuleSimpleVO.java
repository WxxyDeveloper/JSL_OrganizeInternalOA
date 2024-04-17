package com.jsl.oa.model.vodata;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectModuleSimpleVO {
    private String name;
    private String description;
    private Integer workLoad;
    private Timestamp deadLine;
    private String status;
    private String principalUser;

}
