package com.jsl.oa.model.voData;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <h1>用户注册成功UserDO自定义实体类</h1>
 * <hr/>
 * 用于处理用户注册表单输出的数据
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserReturnBackVO {
    private String jobId;
    private String username;
    private String address;
    private String phone;
    private String email;
    private Short age;
    private Short sex;
    private String token;
}
