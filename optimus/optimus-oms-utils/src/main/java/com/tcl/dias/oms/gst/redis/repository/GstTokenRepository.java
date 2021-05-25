package com.tcl.dias.oms.gst.redis.repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.tcl.dias.oms.gst.redis.beans.GstToken;

/**
 * @author archchan
 *
 */
@Service
public class GstTokenRepository {

	public static final String GST_TOKEN_INFO = "GST_TOKEN_INFO";

	private RedisTemplate<String, Object> redisTemplate;

	private SetOperations<String, Object> opsForSet;

	@Autowired
	public GstTokenRepository(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		opsForSet = redisTemplate.opsForSet();
	}

	/**
	 * save- works on redis operations according to token generated and current time
	 * session can be managed
	 * 
	 * @param resetUserInfoBean
	 * @param expireTimeInMillisec
	 * 
	 */
	public void save(GstToken gstToken, long expireTimeInMillisec) {
		opsForSet.add(GST_TOKEN_INFO, gstToken);
		opsForSet.getOperations().expire(GST_TOKEN_INFO, expireTimeInMillisec, TimeUnit.MILLISECONDS);
	}

	/**
	 * method used to find the user based on given userId
	 * 
	 * @param userId
	 * @return Set<>
	 */
	public Set<Object> find() {
		return opsForSet.members(GST_TOKEN_INFO);
	}
}
