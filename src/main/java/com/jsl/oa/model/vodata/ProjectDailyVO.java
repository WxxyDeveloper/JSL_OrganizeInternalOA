package com.jsl.oa.model.vodata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@Accessors(chain = true)
public class ProjectDailyVO {

    private Long id;

    private Long userId;

    private String userName;

    private String nickName;

    private Long projectId;

    private String projectName;

    private String principalName;

    private String content;

    private Date dailyTime;

    private Date createdAt;

    private Date updatedAt;

    private Boolean isAllowDelete;

}


