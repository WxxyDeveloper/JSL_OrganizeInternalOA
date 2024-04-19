package com.jsl.oa.model.vodata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 总体描述
 *
 *  在projectWorkDO上新增了负责人：principalUser，子系统名称：childSystemName
 */
@Data
@Accessors(chain = true)
public class ProjectWorkAndNameVO {

    private Long id;
    private Long projectChildId;
    private Long principalId;
    private String principalUser;
    private Integer cycle;
    private Integer workLoad;
    private String name;
    private String description;
    private String status;
    private Timestamp deadLine;
}


