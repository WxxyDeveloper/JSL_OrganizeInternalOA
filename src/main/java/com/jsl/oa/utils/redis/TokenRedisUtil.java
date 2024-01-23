package com.jsl.oa.utils.redis;

import com.jsl.oa.common.constant.BusinessConstants;
import com.jsl.oa.common.constant.RedisConstant;
import com.jsl.oa.config.redis.RedisOperating;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h1>Token Redis 工具类</h1>
 * <hr/>
 * 用于 Token 的 Redis 操作
 *
 * @param <R> 泛型
 * @version v1.1.0
 * @since v1.1.0
 * @see RedisOperating
 * @see RedisTemplate
 * @see StringRedisTemplate
 * @author xiao_lfeng
 */
@Slf4j
@Component
public class TokenRedisUtil<R> extends RedisOperating<R> {
    public TokenRedisUtil(RedisTemplate<String, R> redisTemplate, StringRedisTemplate stringRedisTemplate) {
        super(redisTemplate, stringRedisTemplate);
    }

    @Override
    public Long getExpiredAt(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_TOKEN + businessConstants.getValue() + field;
        return redisTemplate.getExpire(key);
    }

    @Override
    public Boolean delData(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_TOKEN + businessConstants.getValue() + field;
        return redisTemplate.delete(key);
    }

    @Override
    public R getData(@NotNull BusinessConstants businessConstants, String field) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_TOKEN + businessConstants.getValue() + field;
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean setData(@NotNull BusinessConstants businessConstants, String field, R value, Integer time) {
        // 处理数据
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_TOKEN + businessConstants.getValue() + field;
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
        return true;
    }

    public List<R> getList(@NotNull BusinessConstants businessConstants) {
        String key = RedisConstant.TYPE_AUTH + RedisConstant.TABLE_TOKEN + businessConstants.getValue() + "*";
        return this.getList(key);
    }
}
