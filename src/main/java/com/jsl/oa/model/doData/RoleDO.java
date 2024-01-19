package com.jsl.oa.model.doData;

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
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
public class RoleDO {
    private Long id;
    private String roleName;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
