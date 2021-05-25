package com.tcl.dias.adobesign.authentication.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.tcl.dias.adobesign.constants.AdobeConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * AdobeAuthenticationService - take care of authenticating the adobe sign and
 * provides the access token.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
public class AdobeAuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdobeAuthenticationService.class);

	@Value("${adobesign.authentication.mode}")
	String authenticationMode;

	@Value("${adobesign.authentication.integration.key}")
	String integrationKey;

	@Value("${adobesign.authentication.refresh.token}")
	String refreshToken;

	@Value("${adobesign.authentication.client.id}")
	String clientId;

	@Value("${adobesign.authentication.client.secret}")
	String clientSecret;

	@Value("${adobesign.authentication.redirect.url}")
	String redirectUrl;

	@Value("${adobesign.authentication.base.url}")
	String baseUrl;

	@Value("${adobesign.authentication.refresh.path}")
	String refreshTokenPath;

	@Value("${adobesign.authentication.token.path}")
	String accessTokenPath;

	@Autowired
	RestClientService restClientService;

	/**
	 * 
	 * getAccessToken - generate access token with Bearer appended
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	public String getAccessToken() throws TclCommonException {
		String accessToken = AdobeConstants.BEARER + CommonConstants.SPACE;
		try {
			LOGGER.info("Generating the access Token - Mode : {}", authenticationMode);
			if (authenticationMode.equals(AdobeConstants.INTEGRATION)) {
				accessToken = accessToken + integrationKey;
			} else {
				accessToken = accessToken + generateByOAuth();
			}
		} catch (Exception e) {
			LOGGER.error("Error in generating accesstoken ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return accessToken;
	}

	/**
	 * 
	 * generateByOAuth - generate the access token using the refresh token
	 * 
	 * @return String
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	private String generateByOAuth() throws TclCommonException {
		String refreshTokenUrl = (new StringBuilder(baseUrl)).append(refreshTokenPath).toString();
		LOGGER.info("refresh token Url : {}", refreshTokenUrl);
		LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
		formBody.add(AdobeConstants.CLIENT_ID, clientId);
		formBody.add(AdobeConstants.CLIENT_SECRET, clientSecret);
		formBody.add(AdobeConstants.GRANT_TYPE, AdobeConstants.REFRESH_TOKEN);
		formBody.add(AdobeConstants.REFRESH_TOKEN, refreshToken);
		formBody.add(AdobeConstants.REDIRECT_URI, redirectUrl);
		LOGGER.info("Access Token request using the refresh token , form field : {}", formBody);
		Map<String, String> authHeader = new HashMap<>();
		RestResponse response = restClientService.postWithProxy(refreshTokenUrl, formBody, authHeader);
		if (response.getStatus() == Status.SUCCESS) {
			HashMap<String, String> responseMapper = Utils.convertJsonToObject(response.getData(), HashMap.class);
			LOGGER.info("access token  response received {}", responseMapper);
			return responseMapper.get(AdobeConstants.ACCESS_TOKEN);
		} else {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * generateRefreshTokenByCode -Please follow the below steps to generate the
	 * authorization code - hit the below in the browser and in the redirect url -queryparam copy the code
	 * {@linkplain}https://secure.in1.adobesign.com/public/oauth?redirect_uri=https://www.google.com&response_type=code&client_id=CBJCHBCAABAAT9SUoaTb8M5GGz6xRzdhesKtgII9-DBY&scope=user_login:self+agreement_write:account&state=S6YQD7KDA556DIV6NAU4ELTGSIV26ZNMXDSF7WIEEP0ZLQCLDQ89OYG78C3K9SROC8DXCGRVSGKU1IT1
	 * code is valid only for 5 min 
	 * @param code
	 * @return
	 * @throws TclCommonException
	 */
	@SuppressWarnings("unchecked")
	public String generateRefreshTokenByCode(String code) throws TclCommonException {
		String refreshTokenUrl = (new StringBuilder(baseUrl)).append(accessTokenPath).toString();
		LOGGER.info("access token Url : {}", refreshTokenUrl);
		LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
		formBody.add(AdobeConstants.CLIENT_ID, clientId);
		formBody.add(AdobeConstants.CLIENT_SECRET, clientSecret);
		formBody.add(AdobeConstants.GRANT_TYPE, AdobeConstants.AUTHORIZATION_CODE);
		formBody.add(AdobeConstants.CODE, code);
		formBody.add(AdobeConstants.REDIRECT_URI, redirectUrl);
		LOGGER.info("Access Token request using the refresh token , form field : {}", formBody);
		Map<String, String> authHeader = new HashMap<>();
		authHeader.put("content-type", "application/x-www-form-urlencoded");
		RestResponse response = restClientService.postWithProxy(refreshTokenUrl, formBody, authHeader);
		if (response.getStatus() == Status.SUCCESS) {
			HashMap<String, String> responseMapper = Utils.convertJsonToObject(response.getData(), HashMap.class);
			LOGGER.info("refresh token  response received {}", responseMapper);
			return responseMapper.get(AdobeConstants.REFRESH_TOKEN);
		} else {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}
	}

}
