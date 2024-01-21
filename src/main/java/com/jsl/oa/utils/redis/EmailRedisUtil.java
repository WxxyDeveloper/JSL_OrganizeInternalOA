package com.jsl.oa.utils.redis;

import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.common.constant.RedisConstant;
import com.jsl.oa.config.redis.RedisConfiguration;
import com.jsl.oa.config.redis.RedisOperating;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <h1>Redis工具类</h1>
 * <hr/>
 * 用于操作Redis
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @see RedisConfiguration
 * @see com.jsl.oa.common.constant.RedisConstant
 * @since v1.1.0
 */
@Component
public class EmailRedisUtil<R> extends RedisOperating<R> {

    public EmailRedisUtil(RedisTemplate<String, R> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        super(redisTemplate, stringRedisTemplate);
    }

    /**
     * <h2>获取邮箱验证码过期时间</h2>
     * <hr/>
     * 用于 AuthController 中的 authLoginByEmail 方法<br/>
     * 用于获取邮箱验证码过期时间
     *
     * @param email 邮箱
     * @return 返回邮箱验证码过期时间戳
     */
    @Override
    public Long getExpiredAt(@NotNull BusinessConstants businessConstants, String email) {
        String key = RedisConstant.TYPE_EMAIL + RedisConstant.TABLE_EMAIL + businessConstants.getValue() + email;
        return redisTemplate.getExpire(key);
    }

    /**
     * <h2>删除邮箱验证码</h2>
     * <hr/>
     * 用于 AuthController 中的 authLoginByEmail 方法<br/>
     * 用于删除邮箱验证码
     *
     * @param email 邮箱
     * @return 返回是否删除成功
     */
    @Override
    public Boolean delData(@NotNull BusinessConstants businessConstants, String email) {
        String key = RedisConstant.TYPE_EMAIL + RedisConstant.TABLE_EMAIL + businessConstants.getValue() + email;
        return redisTemplate.delete(key);
    }

    /**
     * <h2>获取邮箱验证码</h2>
     * <hr/>
     * 用于 AuthController 中的 authLoginByEmail 方法<br/>
     * 用于获取邮箱验证码
     *
     * @param email 邮箱
     * @return 返回邮箱验证码
     */
    @Override
    public R getData(@NotNull BusinessConstants businessConstants, String email) {
        String key = RedisConstant.TYPE_EMAIL + RedisConstant.TABLE_EMAIL + businessConstants.getValue() + email;
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * <h2>设置邮箱验证码</h2>
     * <hr/>
     * 用于 AuthController 中的 authLoginByEmail 方法<br/>
     * 用于设置邮箱验证码
     *
     * @param email 邮箱
     * @param value  验证码
     * @return 返回是否添加成功
     */
    @Override
    public Boolean setData(@NotNull BusinessConstants businessConstants, String email, R value, Integer time) {
        // 处理数据
        String key = RedisConstant.TYPE_EMAIL + RedisConstant.TABLE_EMAIL + businessConstants.getValue() + email;
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
        return true;
    }
}
