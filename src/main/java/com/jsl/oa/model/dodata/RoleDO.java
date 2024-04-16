package com.jsl.oa.model.dodata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>role 数据表</h1>
 * <hr/>
 * 映射 oa_role 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 * @version v1.2.0
 */
@Data
@Accessors(chain = true)
public class RoleDO {
    // 角色id
    private Long id;
    // 角色名称
    private String roleName;
    // 中文描述
    private String displayName;
    // 用户组权限
    private String permissions;
    // 创建时间
    private Timestamp createdAt;
    // 修改时间
    private Timestamp updatedAt;
}
