package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>permission 数据表</h1>
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
public class PermissionDO {
    // 主键
    private Long id;
    // 权限名称
    private String name;
    // 权限描述
    private String description;
    // 创建时间
    private Timestamp createdAt;
}
