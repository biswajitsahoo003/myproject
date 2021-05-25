package com.tcl.dias.common.redis.repo;

import java.util.Map;
import java.util.Set;

import com.tcl.dias.common.redis.beans.TokenExpire;

/**
 * 
 * @author Manojkumar R
 *
 */
public interface TokenExpireRepository {

	public void save(TokenExpire tokenExpire);

	public TokenExpire find(String timestamp);
	
	public Set<String> findAllKeys();

	public Map<String, TokenExpire> findAll();

	public void update(TokenExpire tokenExpire);

	public void delete(String timestamp);

}
