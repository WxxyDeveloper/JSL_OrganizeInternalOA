package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>project_cutting 数据表</h1>
 * <hr/>
 * 映射 oa_project_cutting 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectCuttingDO {
    private Long id;
    private Long principalId;
    private Long projectId;
    private Long pid;
    private Long workLoad;
    private Long cycle;
    private String name;
    private String description;
    private Integer is_delete;
    private Integer is_finish;
    private Integer status;
    private boolean type;
    private Timestamp beginTime;
    private Timestamp completeTime;
}
