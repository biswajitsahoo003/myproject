package com.tcl.dias.common.redis.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * RedisCacheServiceImpl to store and retrieve data from redis
 * 
 * @author Samuel.S
 *
 */

@Service
public class RedisCacheServiceImpl implements RedisCacheService {
	public static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);
	
	private @Value("${redis.cache.expiration.seconds:3600}") int cacheExpirationSeconds;
		
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;
	
	private ValueOperations<String,Object> valueOperations;
	private HashOperations<String,Object,Object> hashOperations;
	private ListOperations<String,Object> listOperations;
	private SetOperations<String,Object> setOperations;
	
	@PostConstruct
    private void init(){
		valueOperations = redisTemplate.opsForValue();
		hashOperations = redisTemplate.opsForHash();
		listOperations = redisTemplate.opsForList();
		setOperations = redisTemplate.opsForSet();
    }
	
	@Override
	public void put(String key, Object obj) {	
		valueOperations.set(key,obj,cacheExpirationSeconds,TimeUnit.SECONDS);
	}

	@Override
	public  void put(String key, Object obj, int expirationSeconds) {
		valueOperations.set(key,obj,expirationSeconds,TimeUnit.SECONDS);
	}

	@Override
	public Object get(String key) {
		return valueOperations.get(key);
	}

	@Override
	public Boolean delete(String key) {
		return redisTemplate.delete(key);
	}

	@Override
	public Boolean expire(String key, int expirationSeconds) {
		return redisTemplate.expire(key,expirationSeconds,TimeUnit.SECONDS);
	}
	
	public void putHash(String key, Object hashKey,Object value) {	
		hashOperations.put(key,hashKey,value);
		expire(key,cacheExpirationSeconds);
	}
	
	public void putHash(String key, Object hashKey,Object value,int expirationSeconds) {	
		hashOperations.put(key,hashKey,value);
		expire(key,expirationSeconds);
	}
	
	public Object getHash(String key, Object hashKey) {	
		return hashOperations.get(key,hashKey);
	}
	
	public void addToSet(String key,Object value) {	
		setOperations.add(key,value);
		expire(key,cacheExpirationSeconds);
	}
	
	public void addToSet(String key,Object value,int expirationSeconds) {	
		setOperations.add(key,value);
		expire(key,expirationSeconds);
	}
	public void removeFromSet(String key,Object value) {	
		setOperations.remove(key,value);
	}
	
	public Set<Object> getAllFromSet(String key) {	
		return setOperations.members(key);
	}
	
	public void addToList(String key,Object value) {	
		listOperations.rightPush(key,value);
		expire(key,cacheExpirationSeconds);
	}
	public void addToList(String key,Object value, int expirationSeconds) {	
		listOperations.rightPush(key,value);
		expire(key,expirationSeconds);
	}
	
	public void rmoveFromList(String key,int count,Object value) {	
		listOperations.remove(key,count,value);
	}
	
	public List<Object> getAllFromList(String key,int start, int end) {	
		return listOperations.range(key, start, end);
	}

}
