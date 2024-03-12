package com.jsl.oa.model.doData;

import lombok.Data;

import java.sql.Timestamp;

/**
 * TagProjectDO
 * <hr/>
 * 用于处理标签相关的请求, 包括获取标签列表、编辑标签信息等
 *
 * @since v1.0.0-SNAPSHOT
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Data
public class TagProjectDO {
    private String id;
    private String name;
    private Long pid;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isDelete;
}
