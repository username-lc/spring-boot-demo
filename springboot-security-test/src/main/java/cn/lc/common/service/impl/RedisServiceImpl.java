package cn.lc.common.service.impl;


import cn.lc.common.config.RedisConfig;
import cn.lc.common.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Import(RedisConfig.class)
public class RedisServiceImpl<K,V> implements RedisService<K,V> {


	@Autowired
	@Qualifier(value = "redisTemplate")
	RedisTemplate<K,V> redisTemplate;

	public void setRedisTemplate(RedisTemplate redisTemplate) {
	    this.redisTemplate = redisTemplate;
    }

	/**
	 * 手动释放资源
	 */
	private void releaseConnection() {
		RedisConnectionUtils.unbindConnection(redisTemplate.getConnectionFactory());
	}

	/**
	 * 获取
	 * @param key
	 * @return Object
	 */
	@Override
	public V get(K key) {
		V v = null;
		try{
			v = redisTemplate.opsForValue().get(key);
		}catch (Exception e){
			log.error("redis get error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return v;
	}

	/**
	 * 默认设置 30天有效 避免长期 暂用 redis
	 * @param key
	 * @param value
	 */
	@Override
	public void set(K key, V value) {
		try{
			redisTemplate.opsForValue().set(key, value,3600*24*30, TimeUnit.SECONDS);
		}catch (Exception e){
			log.error("redis set error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}

	}



	/**
	 * 存值并且设置 redis key 过期时间
	 * @param key
	 * @param value
	 * @param timeout
	 */
	@Override
	public void set(K key, V value, long timeout) {
		try{
			redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
		}catch (Exception e){
			log.error("redis set error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
	}

	/**
	 * 存值并且设置 redis key 过期时间
	 * @param key
	 * @param value
	 * @param timeout
	 */
	@Override
	public void set(K key, V value, long timeout,TimeUnit unit) {
		try{
			redisTemplate.opsForValue().set(key, value, timeout, unit);
		}catch (Exception e){
			log.error("redis set error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
	}

	/**
	 * 删除 key
	 * @param key
	 * @return Object
	 */
	@Override
	public void delete(K key) {
		try{
			if (hasKey(key)) {
				redisTemplate.delete(key);
			}
		}catch (Exception e){
			log.error("redis delete error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
	}

	/**
	 * 删除 key
	 *
	 * @param keys
	 * @return Object
	 */
	@Override
	public void delete(Collection<K> keys) {
		try{
			redisTemplate.delete(keys);
		}catch (Exception e){
			log.error("redis delete error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
	}

	/**
	 * 剩余生存时间 (秒单位)
	 * @param key
	 * @return
	 */
	@Override
	public Long getExpire(K key) {
		Long expire = 0L;
		if (!hasKey(key)) {
			return expire;
		}

		try{
			expire = redisTemplate.getExpire(key);
		}catch (Exception e){
			log.error("redis getExpire error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}

		return expire;
	}

	/**
	 * 更新 key 的过期时间
	 * @param key
	 * @param timeout
	 * @param unit
	 * @return
	 */
	@Override
	public boolean setExpire(K key, long timeout, TimeUnit unit) {
		Boolean expire = false;
		try{
			expire = redisTemplate.expire(key, timeout, unit);
		}catch (Exception e){
			log.error("redis setExpire error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return expire;
	}

	/**
	 * 判断 key 是否存在
	 * @param key
	 * @return
	 */
	@Override
	public boolean hasKey(K key) {
		Boolean flag = redisTemplate.hasKey(key);
		try{
			flag = redisTemplate.hasKey(key);
		}catch (Exception e){
			log.error("redis hasKey error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return flag;
	}

	/**
	 * 返回 redisTemplate for Hash的原生 操作对象
	 * @return HashOperations
	 */
	@Override
	public HashOperations redisHash() {
		HashOperations opsForHash = null;
		try{
			opsForHash = redisTemplate.opsForHash();
		}catch (Exception e){
			log.error("redis redisHash error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return opsForHash;
	}

	/**
	 * 返回 redisTemplate for List的原生 操作对象
	 * @return ListOperations
	 */
	@Override
	public ListOperations redisList() {
		ListOperations opsForList = redisTemplate.opsForList();
		try{
			opsForList = redisTemplate.opsForList();
		}catch (Exception e){
			log.error("redis redisList error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return opsForList;
	}
	/**
	 * 返回 redisTemplate for Set的原生 操作对象
	 * @return SetOperations
	 */
	@Override
	public SetOperations redisSet()  {
		SetOperations opsForSet = null;
		try{
			opsForSet = redisTemplate.opsForSet();
		}catch (Exception e){
			log.error("redis redisSet error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return opsForSet;
	}

	/**
	 * 返回 redisTemplate for ZSet的原生 操作对象
	 * @return ZSetOperations
	 */
	@Override
	public ZSetOperations redisZSet()  {
		ZSetOperations opsForZSet = null;
		try{
			opsForZSet = redisTemplate.opsForZSet();
		}catch (Exception e){
			log.error("redis redisZSet error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return opsForZSet;
	}
	/**
	 * 返回 redisTemplate for Value的原生 操作对象
	 * @return ValueOperations
	 */

	@Override
	public ValueOperations redisValue()  {
		ValueOperations opsForValue = null;
		try{
			opsForValue = redisTemplate.opsForValue();
		}catch (Exception e){
			log.error("redis redisValue error,message={}",e.getMessage());
		}finally {
			//释放资源
			releaseConnection();
		}
		return opsForValue;
	}

	/**
	 * /**
	 * 返回 redisTemplat 原生操作对象
	 * return redisTemplate
	 */
	@Override
	public RedisTemplate redisTemplate()  {
		return redisTemplate;
	}


}