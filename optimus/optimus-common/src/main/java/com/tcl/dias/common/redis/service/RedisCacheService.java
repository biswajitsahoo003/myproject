package com.tcl.dias.common.redis.service;

import java.util.List;
import java.util.Set;

/**
 * RedisCacheService to store and retrieve data from redis
 * 
 * @author Samuel.S
 *
 */

public interface RedisCacheService {
	void put(String key, Object value);
	void put(String key, Object value,int expirationSeconds);
	public Object get(String key);
	public Boolean delete(String key);
	public Boolean expire(String key,int expirationSeconds);
	public void putHash(String key, Object hashKey,Object value);	
	public void putHash(String key, Object hashKey,Object value,int expirationSeconds);	
	public Object getHash(String key, Object hashKey);	
	public void addToSet(String key,Object value);	
	public void addToSet(String key,Object value,int expirationSeconds);	
	public Set<Object> getAllFromSet(String key) ;	
	public void addToList(String key,Object value);	
	public void addToList(String key,Object value,int expirationSeconds);
	public List<Object> getAllFromList(String key,int start, int end) ;
}
