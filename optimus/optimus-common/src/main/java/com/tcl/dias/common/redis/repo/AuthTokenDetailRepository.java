package com.tcl.dias.common.redis.repo;

import java.util.Map;

import com.tcl.dias.common.redis.beans.AuthTokenDetail;
import com.tcl.dias.common.redis.beans.UserInformation;

/**
 * 
 * @author Manojkumar R
 *
 */
public interface AuthTokenDetailRepository {

	public void save(UserInformation userInformation,long expireTimeInMillisec);

	public Map<String, Object> find(String userName);

	public void updateAuthToken(AuthTokenDetail authTokenDetail);

	public void updateUserInformation(String primaryToken, UserInformation userInformation);

	public void update(AuthTokenDetail authTokenDetail, UserInformation userInformation);

	public void updateExpiry(String userName, Long timeInMillisec);

	public void delete(String primaryToken);
	
	public void deleteAllToken(String userAgent);

}
