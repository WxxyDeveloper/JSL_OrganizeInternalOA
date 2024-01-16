package com.jsl.oa.common.constant;

import com.jsl.oa.utils.redis.EmailRedisUtil;

/**
 * <h1>Redis常量类</h1>
 * <hr/>
 * 用于存放Redis常量
 *
 * @version v1.1.0
 * @since v1.1.0
 * @see EmailRedisUtil
 * @author 筱锋xiao_lfeng
 */
public class RedisConstant {
    /*
     * 类型分类
     */
    public static final String TYPE_EMAIL = "mail:"; // 邮件相关

    /*
     * 表分类
     */
    public static final String TABLE_EMAIL = "code:"; // 邮箱验证码
}
