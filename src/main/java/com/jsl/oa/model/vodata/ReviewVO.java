package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class ReviewVO {

    private Long id;
    //申请名称
    private String name;
    //申请理由
    private String content;
    //申请者用户id
    private Long senderId;
    //审请者用户名称
    private String senderName;
    //审核者用户id
    private Long recipientId;
    //审核者用户名称
    private String recipientName;
    //审核类别（子系统 子模块）
    private String category;
    //申请的项目id
    private Long projectId;
    //申请的项目名称
    private String projectName;
    //申请的子系统id
    private Long projectChildId;
    //申请的子系统名称
    private String projectChildName;
    //申请的子模块id
    private Long projectModuleId;
    //申请的模块名称
    private String projectModuleName;
    //申请时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applicationTime;
    //审核结果（0：未通过；1：通过；2：未审批）
    private String result;
}


