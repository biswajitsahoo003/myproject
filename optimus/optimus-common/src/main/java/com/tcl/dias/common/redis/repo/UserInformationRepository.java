package com.tcl.dias.common.redis.repo;

import java.util.Map;

import com.tcl.dias.common.redis.beans.UserInformation;

/**
 * 
 * @author Manojkumar R
 *
 */
public interface UserInformationRepository {

	public void save(UserInformation userInformation);

	public UserInformation find(String uid);

	public Map<String, UserInformation> findAll();

	public void update(UserInformation userInformation);

	public void delete(String uid);

}
