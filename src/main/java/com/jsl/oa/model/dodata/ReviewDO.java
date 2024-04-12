package com.jsl.oa.model.dodata;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <h1>oa_review 数据表</h1>
 * <hr/>
 * 映射 oa_permission 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewDO {
    //主键
    private Long id;
    //申请名称
    private String name;
    //申请理由
    private String content;
    //申请者用户id
    private Long senderId;
    //审核者用户id
    private Long recipientId;
    //审核类别（0：子系统；1：子模块）
    private Short category;
    //申请的项目id
    private Long projectId;
    //申请的子系统id
    private Long projectSubsystemId;
    //申请的子模块id
    private Long projectSubmoduleId;
    //申请时间
    private Date applicationTime;
    //审核时间
    private Date reviewTime;
    //审核结果（0：未通过；1：通过；2：未审批）
    private Short reviewResult;
    //是否删除（0：未删除；1：已删除）
    private String isDelete;

}


