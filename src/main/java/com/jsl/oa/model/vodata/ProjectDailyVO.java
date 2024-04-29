package com.jsl.oa.model.vodata;


import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dailyTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;

    private Boolean isAllowDelete;

}


