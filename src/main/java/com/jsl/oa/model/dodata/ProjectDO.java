package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * <h1>project 数据表</h1>
 * <hr/>
 * 映射 oa_project 数据表内容进入自定义实体类, 该实体类用于存储数据表中的数据。
 *
 * @author 筱锋xiao_lfeng
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDO {
    /**
     * 主键
     * <hr/>
     * 主键，自增
     */
    private Long id;
    /**
     * 项目名称
     * <hr/>
     * 项目名称，最长 255 字符
     */
    private String name;
    /**
     * 项目负责人
     * <hr/>
     * 项目负责人，关联 user 表
     */
    private Long principalId;
    /**
     * 项目描述
     * <hr/>
     * 项目描述，需要存储 json 数据
     */
    private String description;
    /**
     * 项目标签
     * <hr/>
     * 项目标签，需要存储 json 数据（项目类型：web，大数据等）
     */
    private String tags;
    /**
     * 项目周期
     * <hr/>
     * 项目周期，单位：天
     */
    private Integer cycle;
    /**
     * 项目工作量
     * <hr/>
     * 项目工作量，单位：人天
     */
    private Integer workLoad;
    /**
     * 项目文件
     * <hr/>
     * 项目文件，需要存储 json 数据（文件名：UUID 生成值）。对于 JSON 内部只需要存储 UUID 信息以及加上文件尾缀即可。
     * <p>
     * 例如：[UUID].pdf, [UUID].png, [UUID].docx
     */
    private String files;
    /**
     * 项目开始时间
     * <hr/>
     * 项目开始时间, 格式：yyyy-MM-dd
     */
    private Date beginTime;
    /**
     * 项目完成时间
     * <hr/>
     * 项目完成时间, 格式：yyyy-MM-dd
     */
    private Date completeTime;
    /**
     * 项目截止时间
     * <hr/>
     * 项目截止时间, 格式：yyyy-MM-dd
     * <p>
     * 项目截止时间为最终的截止时间，即甲方要求的最终结束周期
     */
    private Date deadline;
    /**
     * 项目状态
     * <hr/>
     * 项目状态（draft: 草稿，progress: 进行，pause: 暂停，abnormal: 异常，complete: 完成）
     */
    private String status;
    /**
     * 创建时间
     * <hr/>
     * 创建时间，格式：1234567890123
     */
    private Timestamp createdAt;
    /**
     * 更新时间
     * <hr/>
     * 更新时间，格式：1234567890123
     */
    private Timestamp updatedAt;
    /**
     * 是否删除
     * <hr/>
     * 是否删除（0: 否，1: 是）
     */
    private Boolean isDelete;
}
