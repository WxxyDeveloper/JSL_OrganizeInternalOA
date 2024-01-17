package com.jsl.oa.model.doData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

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
    private String name;
    private String description;
    private String introduction;
    private Short codeOpen;
    private String coreCode;
    private String git;
    private Short difficultyLevel;
    private Integer type;
    private Long reward;
    private Short status;
}
