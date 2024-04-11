package com.jsl.oa.utils.redis;

import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.common.constant.RedisConstant;
import com.jsl.oa.config.redis.RedisOperating;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <h1>角色Redis工具类</h1>
 * <hr/>
 * 角色Redis工具类
 *
 * @since v1.1.0
 * @version v1.1.0
 * @see com.jsl.oa.common.constant.RedisConstant
 * @see com.jsl.oa.common.constant.BusinessConstants
 * @see com.jsl.oa.config.redis.RedisOperating
 * @author xiao_lfeng
 * @param <R> 泛型
 */
@Slf4j
@Component
public class RoleRedisUtil<R> extends RedisOperating<R> {
    public RoleRedisUtil(RedisTemplate<String, R> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        super(redisTemplate, stringRedisTemplate);
    }

    @Override
    public Long getExpiredAt(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_ROLE + businessConstants.getValue() + field;
        log.info("\t\t> 读取 Redis 键为 {} 的过期时间", key);
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean delData(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_ROLE + businessConstants.getValue() + field;
        log.info("\t\t> 删除 Redis 键为 {} 的数据", key);
        return redisTemplate.delete(key);
    }

    @Override
    public R getData(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_ROLE + businessConstants.getValue() + field;
        log.info("\t\t> 读取 Redis 键为 {} 的数据", key);
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean setData(@NotNull BusinessConstants businessConstants, String field, R value, Integer time) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_ROLE + businessConstants.getValue() + field;
        log.info("\t\t> 写入 Redis 键为 {} 的数据", key);
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
        return true;
    }
}
