package com.tcl.dias.auth.redis.repositories;

import java.util.Set;

import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;

/**
 * Used to store user info in redis cache
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface UserToResetTokenRepository {

	public void save(ResetUserInfoBean resetPassword, long expireTimeInMillisec);

	public Set<Object> find(String userId);

	public void delete(String userId, String resetToken);

}
