package com.jsl.oa.model.dodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>meesage 数据表</h1>
 * <hr/>
 * 映射 oa_message 数据表内容进入自定义实体类
 *
 * @author 张睿相
 * @since v1.1.0
 * @version v1.1.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDO {

    private Long id;
    private Long uid;
    private Long sid;
    private String title;
    private String text;
    private Integer isDelete;
    private Timestamp createdAt;
    private Timestamp deletedAt;

}


