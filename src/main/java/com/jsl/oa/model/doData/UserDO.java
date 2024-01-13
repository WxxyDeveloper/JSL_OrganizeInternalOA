package com.jsl.oa.model.doData;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * <h1>user 数据表</h1>
 * <hr/>
 * 映射 oa_user 数据表内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.0.0
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDO {
    private Long id;
    private Long jobId;
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
    private Short age;
    private String signature;
    private String avatar;
    private String nickname;
    private Short sex;
    private Boolean enabled;
    private Boolean accountNoExpired;
    private Boolean credentialsNoExpired;
    private Boolean recommend;
    private Boolean accountNoLocked;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
