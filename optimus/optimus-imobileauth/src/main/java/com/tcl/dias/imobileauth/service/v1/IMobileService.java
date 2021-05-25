package com.tcl.dias.imobileauth.service.v1;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.tcl.common.keycloack.bean.KeycloakUserResponseBean;
import com.tcl.common.keycloack.bean.keycloakAuthBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.keycloack.constants.KeycloakConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.imobileauth.constants.Constants;
import com.tcl.dias.imobileauth.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the IMobileService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class IMobileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IMobileService.class);

	@Value("${keycloak.auth-server-url}")
	String keycloakServerUrl;

	@Value("${keycloak.realm}")
	String realm;

	@Value("${keycloak.resource}")
	String resource;

	@Value("${info.keycloak_realm-url}")
	String realmUrl;

	@Value("${info.keycloak_granttype}")
	String grantType;

	@Value("${info.keycloak_user}")
	String userName;

	@Value("${info.keycloak_pwd}")
	String password;

	@Value("${info.keycloak_clientid}")
	String clientId;

	@Value("${imobile.version}")
	String mobileVersion;

	@Autowired
	RestClientService restClientService;

	/**
	 * 
	 * getMasterRealmCode - This method return the master realm code
	 * 
	 * @return - {@link Map} - gets the code , state and state_session
	 * @throws TclCommonException
	 */
	public Map<String, String> getMasterRealmCode() throws TclCommonException {
		try {
			String user = Utils.getSource();
			LOGGER.info("Initiatizing the get auth token for the user {}", user);
			return getAuthCodeInfo(user);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * getAuthCodeInfo - This method return the master realm code for a given
	 * username
	 * 
	 * @param userName
	 * @return - {@link Map}
	 * @throws TclCommonException
	 */
	private Map<String, String> getAuthCodeInfo(String userName) throws TclCommonException {
		Map<String, String> iresponse = new HashMap<>();
		iresponse.put("version", mobileVersion);
		try {
			LOGGER.info("Initiatizing the impersonate for the user {}", userName);
			HttpHeaders header = impersonateKeycloakUser(userName);
			LOGGER.info("Impersonate successful for the user {}", userName);
			Optional<Map.Entry<String, List<String>>> cookies = header.entrySet().stream()
					.filter(entry -> entry.getKey().equalsIgnoreCase(Constants.SET_COOKIE)).findFirst();
			if (cookies.isPresent()) {
				List<String> rCookie = cookies.get().getValue();
				LOGGER.info("Cookie extracted {}", cookies);
				Map<String, String> kMapper = extractCookies(rCookie);
				LOGGER.info("Data retrived {}", kMapper);
				String lHeader = getKAuthCode(kMapper.get(Constants.STATE), kMapper.get(Constants.KEYCLOAK_IDENTITY),
						kMapper.get(Constants.SESSION));
				if (StringUtils.isNotBlank(lHeader)) {
					String[] splitter = lHeader.split(Constants.EQ_MARK);
					String pathParam = splitter[1];
					String[] pSplitter = pathParam.split(Constants.AMP);
					for (String pSplit : pSplitter) {
						String[] codeSplit = pSplit.split(Constants.EQUALS);
						iresponse.put(codeSplit[0], codeSplit[1]);
					}

				}
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_COOKIE, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return iresponse;
	}

	/**
	 * 
	 * extractCookies - extract the cookie information
	 * 
	 * @param setCookies
	 * @return - {@link Map} - cookie key and value
	 */
	private Map<String, String> extractCookies(List<String> setCookies) {
		LOGGER.info("Inside extracting cookies {}", setCookies);
		Map<String, String> kMapper = new HashMap<>();
		for (String cookies : setCookies) {
			String[] cookieSpliter = cookies.split(CommonConstants.SEMI_COMMA);
			for (String cookie : cookieSpliter) {
				String[] splitter = cookie.split(CommonConstants.EQUAL);
				String key = splitter[0].trim();
				String value = null;

				if (splitter.length == 2) {
					value = splitter[1].trim();
				} else {
					break;
				}
				if (key.equals(Constants.KEYCLOAK_IDENTITY) && StringUtils.isNotBlank(value)) {
					kMapper.put(Constants.STATE, Utils.getSessionState(value));
					kMapper.put(Constants.KEYCLOAK_IDENTITY, value);
				}

				if (key.equals(Constants.KEYCLOAK_SESSION) && StringUtils.isNotBlank(value)) {
					kMapper.put(Constants.SESSION, value);
				}
			}
		}
		return kMapper;
	}

	/**
	 * 
	 * impersonateKeycloakUser - impersonates and provide the keycloak session and
	 * identity cookie
	 * 
	 * @param userName
	 * @return - {@link HttpHeaders}
	 * @throws TclCommonException
	 */
	public HttpHeaders impersonateKeycloakUser(String userName) throws TclCommonException {
		String userId = getKeycloakUserIdByUserName(userName);
		if (userId != null) {
			keycloakAuthBean accessTokenBean = getAdminAuthToken();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			HttpHeaders response = restClientService.postKeyCloakReturnResponseHeaders(
					keycloakServerUrl.concat(realmUrl).concat(Constants.MASTER).concat(Constants.USERS_URL)
							.concat(userId).concat(Constants.IMPERSONATE_URL),
					null, authHeader);
			if (response == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			return response;
		} else {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * getKeycloakUserIdByUserName - This get the keycloak userId
	 * 
	 * @param userName
	 * @return - {@link String}
	 * @throws TclCommonException
	 */
	private String getKeycloakUserIdByUserName(String userName) throws TclCommonException {
		try {
			KeycloakUserResponseBean[] userResponse = null;
			keycloakAuthBean accessTokenBean = getAdminAuthToken();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			String url = keycloakServerUrl.concat(realmUrl).concat(Constants.MASTER).concat(Constants.USER_URL)
					.concat(Constants.Q_MARK).concat(Constants.USERNAME).concat(Constants.EQUALS).concat(userName);
			LOGGER.info("Calling URL {}", url);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(url, authHeader);
			if (getRoleByNameResponse.getData() != null) {
				userResponse = (KeycloakUserResponseBean[]) Utils.convertJsonToObject(getRoleByNameResponse.getData(),
						KeycloakUserResponseBean[].class);
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
			if (userResponse != null) {
				List<KeycloakUserResponseBean> keycloakUserResponseBeans = Arrays.asList(userResponse);
				if (keycloakUserResponseBeans != null && !keycloakUserResponseBeans.isEmpty()) {
					return keycloakUserResponseBeans.get(0).getId();
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * getKAuthCode - gets the k auth code
	 * 
	 * @param state
	 * @param keycloakIdentity
	 * @param keycloakSession
	 * @return {@link String}
	 * @throws TclCommonException
	 */
	public String getKAuthCode(String state, String keycloakIdentity, String keycloakSession)
			throws TclCommonException {
		try {
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(Constants.COOKIE,
					Constants.KEYCLOAK_SESSION.concat(Constants.EQUALS).concat(keycloakSession)
							.concat(Constants.SEMI_COLIN).concat(Constants.KEYCLOAK_IDENTITY).concat(Constants.EQUALS)
							.concat(keycloakIdentity));
			String url = keycloakServerUrl.concat(Constants.AUTH_PATH).concat(Constants.Q_MARK)
					.concat(Constants.STATE.toLowerCase()).concat(Constants.EQUALS).concat(state).concat(Constants.AMP)
					.concat(Constants.RESPONSE_TYPE).concat(Constants.EQUALS).concat(Constants.CODE)
					.concat(Constants.AMP).concat(Constants.CLIENT_ID).concat(Constants.EQUALS).concat(resource)
					.concat(Constants.AMP).concat(Constants.REDIRECT_URI).concat(Constants.EQUALS)
					.concat(keycloakServerUrl);
			LOGGER.info("Calling URL {}", url);
			HttpHeaders restHeaders = restClientService.getHeaders(url, authHeader);
			if (restHeaders != null) {
				List<String> locations = restHeaders.get(Constants.LOCATION);
				LOGGER.info("location header {}", locations);
				for (String location : locations) {
					return location;
				}
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * getAdminAuthToken - gets the admin auth token
	 * 
	 * @return {@link keycloakAuthBean}
	 */
	private keycloakAuthBean getAdminAuthToken() {
		LOGGER.info("Entering get Auth token");
		keycloakAuthBean authToken = null;
		try {
			LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
			formBody.add(KeycloakConstants.CLIENT_ID.toString(), clientId);
			formBody.add(KeycloakConstants.GRANT_TYPE.toString(), grantType);
			formBody.add(KeycloakConstants.PASSWORD.toString(), password);
			formBody.add(KeycloakConstants.USERNAME.toString(), userName);
			RestResponse response = restClientService
					.postWithoutHeader(keycloakServerUrl.concat(Constants.KEYCLOAK_TOKEN_URL), formBody);
			authToken = (keycloakAuthBean) Utils.convertJsonToObject(response.getData(), keycloakAuthBean.class);

		} catch (Exception e) {
			LOGGER.error("Error in getting access token", e);
		}
		return authToken;
	}

}
