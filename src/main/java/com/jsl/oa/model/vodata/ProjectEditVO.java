package com.jsl.oa.model.vodata;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ProjectEditVO {

    private Long id;
    private String name;
    private Long principalId;
    private String tags;
    private Long cycle;
    private String files;
    private String description;
    private String status;
    private Long workLoad;
    private Timestamp beginTime;
    private Timestamp completeTime;
    private Timestamp deadline;

}


