package cn.lc.common.service;

import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * 封装 redisTemplate 接口
 * Created by tomas on 2017/6/13.
 */
public interface RedisService<K,V> {
    /**
     * 获取
     * @param key
     * @return Object
     */
    V get(K key);

    /**
     * 默认设置 30天有效 避免长期 暂用 redis
     * @param key
     * @param value
     */
     void set(K key, V value);

    /**
     * 存值并且设置 redis key 过期时间
     * @param key
     * @param value
     * @param timeout 秒
     */
     void set(K key, V value, long timeout);

    /**
     * 存值并且设置 redis key 过期时间
     * @param key
     * @param value
     * @param timeout
     */
    void set(K key, V value, long timeout, TimeUnit unit);

    /**
     * 删除 key
     * @param key
     * @return Object
     */
    void delete(K key);
    /**
     * 删除 key
     * @param keys
     * @return Object
     */
    void delete(Collection<K> keys);

    /**
     * 剩余生存时间 (秒单位)
     * @param key
     * @return
     */
     Long getExpire(K key);

    /**
     * 更新 key 的过期时间
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
     boolean setExpire(K key, long timeout, TimeUnit unit);
    /**
     * 判断 key 是否存在
     * @param key
     * @return
     */
     boolean hasKey(K key);

    /**
     * 返回 redisTemplate for Hash的原生 操作对象
     * @return HashOperations
     */
     HashOperations redisHash();

    /**
     * 返回 redisTemplate for List的原生 操作对象
     * @return ListOperations
     */
     ListOperations redisList();
    /**
     * 返回 redisTemplate for Set的原生 操作对象
     * @return SetOperations
     */
     SetOperations redisSet();

    /**
     * 返回 redisTemplate for ZSet的原生 操作对象
     * @return ZSetOperations
     */
     ZSetOperations redisZSet() ;

    /**
     * 返回 redisTemplate for Value的原生 操作对象
     * @return ValueOperations
     */
     ValueOperations redisValue();

    /**
     * /**
     * 返回 redisTemplat 原生操作对象
     * return RedisTemplate
     */
     RedisTemplate redisTemplate();
}
