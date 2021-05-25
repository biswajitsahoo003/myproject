package com.tcl.dias.auth.redis.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;
import com.tcl.dias.auth.redis.repositories.ResetUserInfoRepository;

/**
 * responsible to save the user info in redis cache
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ResetUserInfoService implements ResetUserInfoRepository {

	public static final String RESET_USER_INFORMATION = "RESET_USER_INFORMATION";

	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	public ResetUserInfoService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
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
		hashOperations.put(resetUserInfoBean.getResetToken(), RESET_USER_INFORMATION, resetUserInfoBean);
		hashOperations.getOperations().expire(resetUserInfoBean.getResetToken(), expireTimeInMillisec,
				TimeUnit.MILLISECONDS);

	}

	/**
	 * 
	 * This method is used to find the user info based on given token
	 * 
	 * @param resetToken
	 * @return ResetUserInfoBean
	 * 
	 */
	@Override
	public ResetUserInfoBean find(String resetToken) {
		Map<String, Object> response = hashOperations.entries(resetToken);
		if (response != null && !response.isEmpty()) {
			return (ResetUserInfoBean) response.get(RESET_USER_INFORMATION);
		}
		return null;
	}

	/**
	 * deletes the user information based on token
	 * 
	 * @param resetToken
	 * 
	 */
	@Override
	public void delete(String resetToken) {
		hashOperations.delete(resetToken, RESET_USER_INFORMATION);
	}

}
