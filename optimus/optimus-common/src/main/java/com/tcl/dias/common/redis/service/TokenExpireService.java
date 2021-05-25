package com.tcl.dias.common.redis.service;

import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.tcl.dias.common.redis.beans.TokenExpire;
import com.tcl.dias.common.redis.repo.TokenExpireRepository;

/**
 * Token Expire service to persist expiry information of the token in redis
 * 
 * @author Manojkumar R
 *
 */
@Repository
public class TokenExpireService implements TokenExpireRepository {

	private static final String KEY = "TOKEN_EXPIRE";

	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, TokenExpire> hashOperations;

	@Autowired
	public TokenExpireService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(TokenExpire tokenExpire) {
		hashOperations.put(KEY, String.valueOf(tokenExpire.getActualTimeStamp().getTime()), tokenExpire);
	}

	@Override
	public TokenExpire find(String timeStamp) {
		return hashOperations.get(KEY, timeStamp);
	}

	@Override
	public Map<String, TokenExpire> findAll() {
		return hashOperations.entries(KEY);
	}

	@Override
	public void update(TokenExpire tokenExpire) {
		hashOperations.put(KEY, String.valueOf(tokenExpire.getActualTimeStamp().getTime()), tokenExpire);
	}

	@Override
	public void delete(String timeStamp) {
		hashOperations.delete(KEY, timeStamp);
	}

	@Override
	public Set<String> findAllKeys() {
		return hashOperations.keys(KEY);
	}
}
