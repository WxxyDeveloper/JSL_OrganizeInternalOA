package com.jsl.oa.model.vodata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h1>用户注册成功UserDO自定义实体类</h1>
 * <hr/>
 * 用于处理用户注册表单输出的数据
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Data
@Accessors(chain = true)
public class UserReturnBackVO {
    private ReturnUser user;
    private ReturnUserRole role;
    private String token;
    private List<String> permission;

    @Data
    @Accessors(chain = true)
    public static class ReturnUserRole {
        private Long rid;
    }

    @Data
    @Accessors(chain = true)
    public static class ReturnUser {
        private Long id;
        private String jobId;
        private String username;
        private String phone;
        private String email;
    }
}
