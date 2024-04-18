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
public class ProjectChildDO {
    private Long id;
    private Long principalId;
    private Long projectId;
    private Integer workLoad;
    private Integer cycle;
    private String name;
    private String description;
    private Integer isDelete;
    private String files;
    private Timestamp createdAt;
    private Timestamp completeTime;
    private Timestamp updatedAt;
    private String status;
    private Timestamp deadLine;
}
