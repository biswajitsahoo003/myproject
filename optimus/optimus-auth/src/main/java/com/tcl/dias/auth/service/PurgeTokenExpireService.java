package com.tcl.dias.auth.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.beans.TokenExpire;
import com.tcl.dias.common.redis.service.AuthTokenService;
import com.tcl.dias.common.redis.service.TokenExpireService;

@Service
public class PurgeTokenExpireService {

	@Autowired
	private TokenExpireService tokenExpireService;

	@Autowired
	private AuthTokenService authTokenService;

	@Value("${token.expiry.time}")
	private long expiryTime;

	@Value("${token.purge.time}")
	private long purgeTime;

	/**
	 * processExpiredTokenPurging
	 */
	public void processExpiredTokenPurging() {
		Set<String> keys = tokenExpireService.findAllKeys();
		if (!keys.isEmpty()) {
			keys.parallelStream().forEach(timeStamp -> {
				TokenExpire tokenExpire = tokenExpireService.find(timeStamp);
				long time = Long.parseLong(timeStamp);
				long currentTime = new Date().getTime();
				if (!tokenExpire.isChild()) {
					if ((currentTime - time) >= expiryTime) {
						String token = tokenExpire.getToken();
						authTokenService.delete(token);
						tokenExpireService.delete(timeStamp);
					}
				} else {
					if ((currentTime - time) >= purgeTime) {
						String token = tokenExpire.getToken();
						authTokenService.delete(token);
						tokenExpireService.delete(timeStamp);
					}
				}
			});
		}
	}

}
