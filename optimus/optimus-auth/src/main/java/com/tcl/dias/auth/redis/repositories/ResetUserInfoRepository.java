package com.tcl.dias.auth.redis.repositories;

import com.tcl.dias.auth.redis.beans.ResetUserInfoBean;

/**
 * Used to be saved user details in redis cache
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface ResetUserInfoRepository {

	public void save(ResetUserInfoBean resetPassword, long expireTimeInMillisec);

	public ResetUserInfoBean find(String resetToken);

	public void delete(String resetToken);

}
