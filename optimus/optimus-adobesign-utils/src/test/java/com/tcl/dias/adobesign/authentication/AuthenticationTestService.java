package com.tcl.dias.adobesign.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tcl.dias.adobesign.authentication.service.AdobeAuthenticationService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the AuthenticationTestService.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationTestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTestService.class);

	@Autowired
	AdobeAuthenticationService adobeAuthenticationService;

	//@Test
	public void testAccessToken() {
		try {
			String accessToken = adobeAuthenticationService.getAccessToken();
			LOGGER.info("Access Token {}", accessToken);
		} catch (TclCommonException e) {
			LOGGER.error("Error in Access Token", e);
		}
	}
	
	@Test
	public void testRefreshToken() {
		try {
			String accessToken = adobeAuthenticationService.generateRefreshTokenByCode("CBNCKBAAHBCAABAA4SWSHcq1G5xspzAynQ3WcEzA3_8BOENg");
			LOGGER.info("Refresh Token {}", accessToken);
		} catch (TclCommonException e) {
			LOGGER.error("Error in Access Token", e);
		}
	}

}
