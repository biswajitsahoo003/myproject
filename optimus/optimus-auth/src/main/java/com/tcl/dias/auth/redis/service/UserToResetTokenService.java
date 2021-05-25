package com.tcl.dias.auth.redis.service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;
import com.tcl.dias.auth.redis.repositories.UserToResetTokenRepository;

/**
 * responsible to save the user info in redis cache
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class UserToResetTokenService implements UserToResetTokenRepository {

	private RedisTemplate<String, Object> redisTemplate;

	private SetOperations<String, Object> opsForSet;
	
	private static final String APPENDER="FORGET_PWD_APD";

	@Autowired
	public UserToResetTokenService(RedisTemplate<String, Object> redisTemplate) {
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
	@Override
	public void save(ResetUserInfoBean resetUserInfoBean, long expireTimeInMillisec) {
		opsForSet.add(APPENDER+resetUserInfoBean.getUserId(), resetUserInfoBean.getResetToken());
		opsForSet.getOperations().expire(APPENDER+resetUserInfoBean.getUserId(), expireTimeInMillisec, TimeUnit.MILLISECONDS);
	}

	/**
	 * method used to find the user based on given userId
	 * 
	 * @param userId
	 * @return Set<>
	 */
	@Override
	public Set<Object> find(String userId) {
		return opsForSet.members(APPENDER+userId);
	}

	/**
	 * deletes the user information based on token
	 * @param userId
	 * @param resetToken
	 * 
	 */
	@Override
	public void delete(String userId, String resetToken) {
		opsForSet.remove(APPENDER+userId, resetToken);

	}
}
