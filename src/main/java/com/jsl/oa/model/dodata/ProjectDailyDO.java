package com.jsl.oa.model.dodata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 项目日报(ProjectDaily)实体类
 *
 * @author zrx
 * @since 2024-04-18 11:40:56
 */
@Data
@Accessors(chain = true)
public class ProjectDailyDO {
    
    /**
     * 日报主键 自增
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 日报内容
     */
    private String content;
    /**
     * 日志发布时间
     */
    private Date dailyTime;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;
    /**
     * 是否删除
     */
    private Integer isDelete;

}



