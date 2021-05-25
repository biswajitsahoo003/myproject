package com.tcl.dias.common.redis.service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import com.tcl.dias.common.redis.beans.AuthTokenDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;

/**
 * Authentication token persistance class interacts with redis cache for
 * save/update/delete operations
 * 
 * @author Manojkumar R
 *
 */
@Repository
public class AuthTokenService implements AuthTokenDetailRepository {

	public static final String TOKEN = "TOKEN";
	public static final String USER_INFORMATION = "USER_INFORMATION";

	private RedisTemplate<String, Object> redisTemplate;

	private HashOperations<String, String, Object> hashOperations;
	SetOperations<String, Object> listOps;

	@Autowired
	public AuthTokenService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
		listOps = redisTemplate.opsForSet();
	}

	/*
	 * Saving token and user details in the redis cache
	 */
	public void save(UserInformation userInformation, long expireTimeInMillisec) {
		hashOperations.put(userInformation.getUserId(), USER_INFORMATION, userInformation);
		hashOperations.getOperations().expire(userInformation.getUserId(), expireTimeInMillisec, TimeUnit.MILLISECONDS);
	}

	/*
	 * Retrieving the saved information by passing the authentication token
	 */
	@Override
	public Map<String, Object> find(String userName) {
		return hashOperations.entries(userName);
	}

	/*
	 * Updating the authentication token details
	 */
	@Override
	public void updateAuthToken(AuthTokenDetail authTokenDetail) {
		hashOperations.put(authTokenDetail.getPrimaryToken(), TOKEN, authTokenDetail);
		listOps.add(authTokenDetail.getUserAgent() + authTokenDetail.getxForwardedIp(),
				authTokenDetail.getPrimaryToken());
	}

	/*
	 * Updating the user information of the primary token in redis
	 */
	public void updateUserInformation(String primaryToken, UserInformation userInformation) {
		hashOperations.put(primaryToken, USER_INFORMATION, userInformation);
	}

	/*
	 * Updating the secondary token and user information of the primary token
	 */
	public void update(AuthTokenDetail authTokenDetail, UserInformation userInformation) {
		hashOperations.put(authTokenDetail.getPrimaryToken(), TOKEN, authTokenDetail);
		hashOperations.put(authTokenDetail.getPrimaryToken(), USER_INFORMATION, userInformation);
	}

	/*
	 * Updating the primary token expiry timestamp after the secondary token got
	 * generated
	 */
	public void updateExpiry(String userName, Long timeInMillisec) {
		hashOperations.getOperations().expire(userName, timeInMillisec, TimeUnit.MILLISECONDS);
	}

	/*
	 * Deleting the token from redis cache
	 */
	@Override
	public void delete(String primaryToken) {
		hashOperations.delete(primaryToken, TOKEN, USER_INFORMATION);
	}

	/**
	 * deleteAllToken
	 */
	public void deleteAllToken(String userAgent) {
		Set<Object> tokens = listOps.members(userAgent);
		for (Object token : tokens) {
			hashOperations.delete((String) token, TOKEN, USER_INFORMATION);
		}
		listOps.remove(userAgent, tokens);
	}
}
