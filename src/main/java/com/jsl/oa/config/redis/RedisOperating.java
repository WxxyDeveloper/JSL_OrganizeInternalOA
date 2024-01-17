package com.jsl.oa.config.redis;

import com.jsl.oa.common.constant.BusinessConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class RedisOperating<R> {
    protected final RedisTemplate<String, R> redisTemplate;
    protected final StringRedisTemplate stringRedisTemplate;

    public abstract Long getExpiredAt(BusinessConstants businessConstants, String field);
    public abstract Boolean delData(BusinessConstants businessConstants, String field);
    public abstract R getData(BusinessConstants businessConstants, String field);
    public abstract Boolean setData(BusinessConstants businessConstants, String field, R value, Integer time);

    /**
     * <h2>获取Redis中元素过期时间</h2>
     * <hr/>
     * 基础方法，用于添加String元素到Redis<br/>
     *
     * @param key 索引
     * @return 返回过期时间
     */
    public Long getExpiredAt(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * <h2>基础添加String元素到Redis</h2>
     * <hr/>
     * 基础方法，用于添加String元素到Redis<br/>
     * 默认处理时间，单位时间秒
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value, Integer time) {
        stringRedisTemplate.opsForValue().set(key, value);
        stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * <h2>基础添加元素到Redis</h2>
     * <hr/>
     * 基础方法，用于添加元素元素到Redis<br/>
     * 默认处理时间，单位时间秒
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, R value, Integer time) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * <h2>基础从Redis获取List</h2>
     * <hr/>
     * 基础方法，用于从Redis获取List<br/>
     *
     * @param pattern 正则表达式
     * @return 返回List
     */
    public List<R> getList(String pattern) {
        // 获取全部匹配的key
        Set<String> keys = stringRedisTemplate.keys(pattern);
        // 获取全部匹配的value
        if (keys != null) {
            return redisTemplate.opsForValue().multiGet(keys);
        } else {
            return null;
        }
    }
}