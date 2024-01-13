package com.jsl.oa.model.doData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Date;

/**
 * <h1>users 数据表</h1>
 * <hr/>
 * 映射 users 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @since v1.0.0
 * @version v1.0.0
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDO {
    private Integer id;
    private String userNum;
    private String username;
    private String password;
    private String sex;
    private Date age;
    private String unit;
    private String filed;
    private String hometown;
    private String kind;
    private String state;
}
