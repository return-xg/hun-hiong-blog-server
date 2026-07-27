package com.hunhiong.blog.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作封装
 *
 * @author hunhiong
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    // ============ 通用 ============

    /**
     * 设置缓存
     */
    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Redis set 失败, key={}", key, e);
        }
    }

    /**
     * 设置缓存并设置过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis set(带过期) 失败, key={}", key, e);
        }
    }

    /**
     * 设置缓存并设置过期时间
     */
    public void set(String key, Object value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            log.error("Redis set(Duration) 失败, key={}", key, e);
        }
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis get 失败, key={}", key, e);
            return null;
        }
    }

    /**
     * 获取原始 Object
     */
    public Object getObject(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Redis getObject 失败, key={}", key, e);
            return null;
        }
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete 失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 批量删除缓存
     */
    public Long delete(Collection<String> keys) {
        try {
            return redisTemplate.delete(keys);
        } catch (Exception e) {
            log.error("Redis 批量 delete 失败, keys={}", keys, e);
            return 0L;
        }
    }

    /**
     * 设置过期时间（秒）
     */
    public Boolean expire(String key, long timeout) {
        try {
            return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis expire 失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, Duration timeout) {
        try {
            return redisTemplate.expire(key, timeout);
        } catch (Exception e) {
            log.error("Redis expire(Duration) 失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 获取剩余过期时间（秒）
     */
    public Long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Redis getExpire 失败, key={}", key, e);
            return -1L;
        }
    }

    /**
     * 判断 key 是否存在
     */
    public Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Redis hasKey 失败, key={}", key, e);
            return false;
        }
    }

    /**
     * 递增
     */
    public Long increment(String key, long delta) {
        try {
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            log.error("Redis increment 失败, key={}", key, e);
            return 0L;
        }
    }

    // ============ Hash 操作 ============

    /**
     * Hash 字段递增
     *
     * @param key   Hash Key
     * @param field 字段
     * @param delta 增量
     * @return 递增后的值
     */
    public Long incrementHash(String key, String field, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, field, delta);
        } catch (Exception e) {
            log.error("Redis incrementHash 失败, key={}, field={}", key, field, e);
            return 0L;
        }
    }

    /**
     * 获取 Hash 中所有字段和值
     *
     * @param key Hash Key
     * @return 字段与值的映射
     */
    public Map<Object, Object> getHashEntries(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            log.error("Redis getHashEntries 失败, key={}", key, e);
            return Map.of();
        }
    }
}
