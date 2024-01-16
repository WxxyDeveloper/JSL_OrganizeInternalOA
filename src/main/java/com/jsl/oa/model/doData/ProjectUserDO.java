package com.jsl.oa.model.doData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>project_user 数据表</h1>
 * <hr/>
 * 映射 oa_project_user 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectUserDO {
    private Long id;
    private Long pid;
    private Long uid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
