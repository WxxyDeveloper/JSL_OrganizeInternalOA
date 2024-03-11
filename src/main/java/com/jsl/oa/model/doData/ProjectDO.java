package com.jsl.oa.model.doData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>project 数据表</h1>
 * <hr/>
 * 映射 oa_project 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDO {
    private Long id;
    private Long principalId;
    private String tags;
    private Long cycle;
    private String name;
    private String file;
    private String description;
    private Integer isDelete;
    private Integer isFinish;
    private boolean status;
    private Timestamp beginTime;
    private Timestamp completeTime;
    private Timestamp deadline;
}
