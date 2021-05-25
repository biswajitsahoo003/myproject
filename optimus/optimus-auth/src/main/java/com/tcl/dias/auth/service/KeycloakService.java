package com.tcl.dias.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.tcl.common.keycloack.bean.KeyCloackRoles;
import com.tcl.common.keycloack.bean.KeycloakUserBean;
import com.tcl.common.keycloack.bean.KeycloakUserResponseBean;
import com.tcl.common.keycloack.bean.RolesBean;
import com.tcl.common.keycloack.bean.UserRoleMappingBean;
import com.tcl.common.keycloack.bean.keycloakAuthBean;
import com.tcl.dias.auth.beans.KeycloakResetPasswordBean;
import com.tcl.dias.auth.constants.Constants;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.common.beans.ChangePasswordBean;
import com.tcl.dias.common.beans.KeycloakIdentityProvider;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.beans.UserManagementBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.keycloack.constants.KeycloakConstants;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the KeycloakService.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class KeycloakService {

	@Value("${keycloak.auth-server-url}")
	String keycloakServerUrl;

	@Value("${keycloak.realm}")
	String realm;

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

	@Value("${info.keycloak_resetpassword}")
	String passwordResetUrl;

	@Autowired
	RestClientService restClientService;

	@Autowired
	UserService userService;

	private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakService.class);

	/**
	 * @author ANANDHI VIJAY getAuthToke
	 * @return
	 */
	public keycloakAuthBean getAuthToke() {
		keycloakAuthBean authToken = null;

		try {
			LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
			formBody.add(KeycloakConstants.CLIENT_ID.toString(), clientId);
			formBody.add(KeycloakConstants.GRANT_TYPE.toString(), grantType);
			formBody.add(KeycloakConstants.PASSWORD.toString(), password);
			formBody.add(KeycloakConstants.USERNAME.toString(), userName);
			LOGGER.info("token request for keycloack {}", formBody);
			RestResponse response = restClientService
					.postWithoutHeader(keycloakServerUrl.concat(Constants.KEYCLOAK_TOKEN_URL), formBody);

			LOGGER.info("token respone from keycloak {}", response);

			authToken = (keycloakAuthBean) Utils.convertJsonToObject(response.getData(), keycloakAuthBean.class);

		} catch (Exception e) {
			LOGGER.error("Error in getting access token", e);
		}
		return authToken;
	}

	/**
	 * 
	 * createRoles
	 * 
	 * @param role
	 * @return
	 * @throws TclCommonException
	 */
	public RolesBean createRoles(RolesBean inputData) throws TclCommonException {
		try {
			RolesBean role = constructRolesBeanForAddRole(inputData);
			String request = Utils.convertObjectToJson(role);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			Utils.disableSslVerification();
			RestResponse createRolesResponse = restClientService.postKeyCloak(
					keycloakServerUrl.concat(realmUrl.concat(realm).concat(Constants.ROLES_URL)), request, authHeader);
			if (createRolesResponse.getStatus() == Status.SUCCESS) {
				return role;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_CREATING_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * createUser
	 * 
	 * @param user
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean createUser(KeycloakUserBean user, List<String> roles, String password) throws TclCommonException {
		try {
			String request = Utils.convertObjectToJson(user);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse createRolesResponse = restClientService.postKeyCloak(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.USER_URL), request, authHeader);
			if (createRolesResponse.getStatus() == Status.SUCCESS) {
				String userId = getKeycloakUserIdByUserName(user.getUsername());
				// Password
				setPasswordForUser(password, userId);
				addUserRoleToTheUser(userId, getAndConstructRolesBean(roles));
				if (user.getEmail().toLowerCase().contains(Constants.TATA_COM_DOMAIN)) {
				createSamlIdentityProviderForTheUser(userId, user.getEmail());
				}
				return true;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_CREATING_USER,ex, ResponseResource.R_CODE_ERROR);
		}
		return false;
	}

	/**
	 * 
	 * deleteUser
	 * 
	 * @param userId
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean deleteUser(String userId) throws TclCommonException {
		try {
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse createRolesResponse = restClientService.deleteKeyCloak(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.USERS_URL).concat(userId),
					authHeader);
			if (createRolesResponse.getStatus() == Status.SUCCESS) {
				return true;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_DELETING_USER,ex, ResponseResource.R_CODE_ERROR);
		}
		return false;
	}

	/**
	 * 
	 * addUserRoleToTheUser
	 * 
	 * @param userId
	 * @param roleName
	 * @return
	 * @throws TclCommonException
	 */
	public String addUserRoleToTheUser(String userId, List<RolesBean> roles) throws TclCommonException {
		try {
			String request = Utils.convertObjectToJson(constructUserRoleMappingBean(roles));
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse createRolesResponse = restClientService.postKeyCloak(keycloakServerUrl.concat(realmUrl)
					.concat(realm).concat(Constants.USERS_URL).concat(userId).concat(Constants.ROLES_MAPPING_URL),
					request, authHeader);
			if (createRolesResponse.getStatus() == Status.SUCCESS) {
				return createRolesResponse.getData();
			}
		} catch (Exception ex) {
			LOGGER.info("Exception in role mapping");
		}
		return null;
	}

	/**
	 * 
	 * deleteRoleForTheUser
	 * 
	 * @param userId
	 * @param roleName
	 * @return
	 * @throws TclCommonException
	 */
	public String deleteRoleForTheUser(String userId, List<RolesBean> roles) throws TclCommonException {
		try {
			String request = Utils.convertObjectToJson(constructUserRoleMappingBean(roles));
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse createRolesResponse = restClientService.deleteKeyCloakWithRequest(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.USERS_URL).concat(userId)
							.concat(Constants.ROLES_MAPPING_URL),
					request, authHeader);
			if (createRolesResponse.getStatus() == Status.SUCCESS) {
				return createRolesResponse.getData();
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_REMOVE_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * getRoleByName
	 * 
	 * @param roleName
	 * @return
	 * @throws TclCommonException
	 */
	public RolesBean getRoleByName(String roleName) throws TclCommonException {
		try {
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(keycloakServerUrl.concat(realmUrl)
					.concat(realm).concat(Constants.ROLES_URL_WITH_SLASH).concat(roleName), authHeader);
			if (getRoleByNameResponse.getData() != null) {
				return (RolesBean) Utils.convertJsonToObject(getRoleByNameResponse.getData(), RolesBean.class);
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY getAllRoles
	 * @return KeyCloackRoles
	 * @throws TclCommonException
	 */
	public KeyCloackRoles getAllRoles() throws TclCommonException {
		KeyCloackRoles keyCloackRoles = new KeyCloackRoles();
		try {

			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse response = restClientService.getCallForKeycloak(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.ROLES_URL_WITH_SLASH),
					authHeader);
			if (response.getData() != null) {

				RolesBean[] roles = (RolesBean[]) Utils.convertJsonToObject(response.getData(), RolesBean[].class);

				if (roles != null && roles.length > 0) {
					List<RolesBean> rolesList = Arrays.asList(roles);
					rolesList.stream().forEach(rolesBean -> {
						RolesBean role = new RolesBean();
						role.setClientRole(rolesBean.getClientRole());
						role.setId(rolesBean.getId());
						role.setContainerId(rolesBean.getContainerId());
						role.setDescription(rolesBean.getDescription());
						role.setComposite(rolesBean.getComposite());
						role.setName(rolesBean.getName());
						keyCloackRoles.getRoles().add(role);
					});
				}

			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
			return keyCloackRoles;
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * 
	 * constructUserRoleMappingBean
	 * 
	 * @param role
	 * @return
	 */
	public List<UserRoleMappingBean> constructUserRoleMappingBean(List<RolesBean> roles) {
		List<UserRoleMappingBean> roleList = new ArrayList<>();
		roles.stream().forEach(role -> {

			UserRoleMappingBean userRoleMappingBean = new UserRoleMappingBean();
			userRoleMappingBean.setId(role.getId());
			userRoleMappingBean.setName(role.getName());
			userRoleMappingBean.setComposite(role.getComposite());
			userRoleMappingBean.setClientRole(role.getClientRole());
			userRoleMappingBean.setContainerId(realm);
			roleList.add(userRoleMappingBean);

		});
		return roleList;
	}

	/**
	 * @author ANANDHI VIJAY constructKeycloakUserBean
	 * @param userManagementBean
	 * @return KeycloakUserBean
	 */
	public KeycloakUserBean constructKeycloakUserBean(UserManagementBean userManagementBean) {
		KeycloakUserBean userBean = new KeycloakUserBean();
		userBean.setEmail(userManagementBean.getEmail());
		userBean.setEnabled(true);
		userBean.setFirstName(userManagementBean.getFirstName());
		userBean.setLastName(userManagementBean.getLastName());
		userBean.setUsername(userManagementBean.getUserName());
		return userBean;
	}

	/**
	 * @author ANANDHI VIJAY getKeycloakUserIdByUserName
	 * @param userName
	 * @return String
	 * @throws TclCommonException
	 */
	public String getKeycloakUserIdByUserName(String userName) throws TclCommonException {
		try {
			KeycloakUserResponseBean[] userResponse = null;
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(keycloakServerUrl.concat(realmUrl)
					.concat(realm).concat(Constants.USER_URL).concat("?username=").concat(userName), authHeader);
			if (getRoleByNameResponse.getData() != null) {
				userResponse = (KeycloakUserResponseBean[]) Utils.convertJsonToObject(getRoleByNameResponse.getData(),
						KeycloakUserResponseBean[].class);
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
			if (userResponse != null) {
				List<KeycloakUserResponseBean> keycloakUserResponseBeans = Arrays.asList(userResponse);
				for (KeycloakUserResponseBean keycloakUserResponseBean : keycloakUserResponseBeans) {
					if(keycloakUserResponseBean.getUsername().equalsIgnoreCase(userName)) {
						return keycloakUserResponseBean.getId();
					}
					
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY addOrRemoveRolesToTheUser
	 * @param userName
	 * @return List<RolesBean>
	 * @throws TclCommonException
	 */
	public List<RolesBean> getRolesPresentToTheUser(String userId) throws TclCommonException {
		List<RolesBean> rolesBeans = new ArrayList<>();
		try {
			RolesBean[] rolesMappedCurrently = null;

			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(keycloakServerUrl.concat(realmUrl)
					.concat(realm).concat(Constants.USERS_URL).concat(userId).concat(Constants.ROLES_MAPPING_URL),
					authHeader);
			if (getRoleByNameResponse.getData() != null) {
				rolesMappedCurrently = (RolesBean[]) Utils.convertJsonToObject(getRoleByNameResponse.getData(),
						RolesBean[].class);
				if (rolesMappedCurrently != null && rolesMappedCurrently.length > 0) {
					rolesBeans = Arrays.asList(rolesMappedCurrently);
				}
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,e, ResponseResource.R_CODE_ERROR);
		}
		return rolesBeans;
	}

	/**
	 * @author ANANDHI VIJAY addOrRemoveTheRoles
	 * @param userName
	 * @param roles
	 * @return String
	 * @throws TclCommonException
	 */
	public String addOrRemoveTheRoles(String userName, List<String> roles) throws TclCommonException {
		if (userName == null) {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
		}
		try {
			String userId = getKeycloakUserIdByUserName(userName);
			if (userId == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			List<RolesBean> rolesMapped = getRolesPresentToTheUser(userId);
			if (rolesMapped == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE, ResponseResource.R_CODE_ERROR);
			} else {
				// Delete present roles
				List<RolesBean> rolesToBeDeleted = new ArrayList<>();
				rolesMapped.stream().forEach(rolesPresent -> {
					if (!rolesPresent.getName().equals("uma_authorization")
							&& !rolesPresent.getName().equals("offline_access")) {
						rolesToBeDeleted.add(rolesPresent);
					}
				});
				if (!rolesToBeDeleted.isEmpty()) {
					deleteRoleForTheUser(userId, rolesToBeDeleted);
				}
				addUserRoleToTheUser(userId, getAndConstructRolesBean(roles));
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY deleteTheUser
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public void deleteTheUser(String userName) throws TclCommonException {
		if (userName == null) {
			throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
		}
		try {
			String userId = getKeycloakUserIdByUserName(userName);
			if (userId != null) {
				deleteUser(userId);
			}
			
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY getAndConstructRolesBean
	 * @param roles
	 * @return
	 * @throws TclCommonException
	 */
	public List<RolesBean> getAndConstructRolesBean(List<String> roles) throws TclCommonException {
		KeyCloackRoles rolesForRealm = getAllRoles();
		List<RolesBean> rolesList = new ArrayList<>();
		List<RolesBean> rolesListAfterSearch = new ArrayList<>();
		try {
			if (rolesForRealm != null && rolesForRealm.getRoles() != null && !rolesForRealm.getRoles().isEmpty()) {
				rolesList = rolesForRealm.getRoles();
			}
			if (!rolesList.isEmpty()) {
				for (String inputRole : roles) {
					for (RolesBean role : rolesList) {
						if (role.getName().equals(inputRole)) {
							rolesListAfterSearch.add(role);
							break;
						}
					}
				}
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
		return rolesListAfterSearch;
	}

	/**
	 * @author ANANDHI VIJAY constructRolesBeanForAddRole
	 * @param inputData
	 * @return
	 */
	public RolesBean constructRolesBeanForAddRole(RolesBean inputData) {
		RolesBean role = new RolesBean();
		role.setName(inputData.getName());
		role.setComposite(false);
		role.setClientRole(false);
		role.setContainerId(realm);
		return role;
	}

	/**
	 * @author ANANDHI VIJAY getUserDetailsByUserName
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getUserRoleDetailsByUserName(String userName) throws TclCommonException {
		String userId = getKeycloakUserIdByUserName(userName);
		List<String> rolesNameList = new ArrayList<>();
		if (userId != null) {
			List<RolesBean> rolesList = getRolesPresentToTheUser(userId);
			if (rolesList != null && !rolesList.isEmpty()) {
				rolesList.stream().forEach(role -> {
					if (role.getName().contains("optimus") || role.getName().contains("OPTIMUS")) {
						rolesNameList.add(role.getName());
					}
				});
				return rolesNameList;
			}
		}
		return rolesNameList;
	}

	/**
	 * @author ANANDHI VIJAY setPasswordForUser
	 * @param password
	 * @param userId
	 * @throws TclCommonException
	 */
	public void setPasswordForUser(String password, String userId) throws TclCommonException {
		try {
			KeycloakResetPasswordBean resetPassordBean = constructResetPasswordBean(password);
			String request = Utils.convertObjectToJson(resetPassordBean);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse response = restClientService.putKeyCloak(keycloakServerUrl.concat(realmUrl).concat(realm)
					.concat(Constants.USERS_URL).concat(userId).concat(passwordResetUrl), request, authHeader);
			if (!response.getStatus().equals(Status.SUCCESS)) {
				throw new TclCommonException(ExceptionConstants.ERROR_CREATE_PD, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * @author ANANDHI VIJAY constructResetPasswordBean
	 * @param value
	 * @return
	 */
	public KeycloakResetPasswordBean constructResetPasswordBean(String value) {
		KeycloakResetPasswordBean bean = new KeycloakResetPasswordBean();
		bean.setTemporary(false);
		bean.setValue(value);
		bean.setType(grantType);
		return bean;
	}

	/**
	 * @author ANANDHI VIJAY changeUserPassword
	 * @param changePasswordBean
	 * @return
	 * @throws TclCommonException
	 */
	public ChangePasswordBean changeUserPassword(ChangePasswordBean changePasswordBean) throws TclCommonException {
		try {
			String userId = getKeycloakUserIdByUserName(changePasswordBean.getUserName());
			if (userId == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			if (verifyOldPassword(changePasswordBean.getUserName(), changePasswordBean.getOldPassword())) {
				setPasswordForUser(changePasswordBean.getNewPassword(), userId);
				UserManagementBean user = new UserManagementBean();
				user.setUserName(changePasswordBean.getUserName());
				user.setForceChangePassword((byte) 0);
				userService.updateUser(user);
			} else {
				throw new TclCommonException(ExceptionConstants.INCORRECT_OLD_PD, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in changeUserPassword {} ", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return changePasswordBean;
	}

	/**
	 * @author ANANDHI VIJAY verifyOldPassword
	 * @param userName
	 * @param oldPassword
	 * @return
	 */
	public Boolean verifyOldPassword(String userName, String oldPassword) {
		Boolean isCorrect = false;

		keycloakAuthBean authToken = null;

		try {
			LinkedMultiValueMap<String, Object> formBody = new LinkedMultiValueMap<>();
			formBody.add(KeycloakConstants.CLIENT_ID.toString(), clientId);
			formBody.add(KeycloakConstants.GRANT_TYPE.toString(), grantType);
			formBody.add(KeycloakConstants.PASSWORD.toString(), oldPassword);
			formBody.add(KeycloakConstants.USERNAME.toString(), userName);
			LOGGER.info("token request for keycloack {}", formBody);
			RestResponse response = restClientService
					.postWithoutHeader(keycloakServerUrl.concat(Constants.KEYCLOAK_TOKEN_URL), formBody);

			LOGGER.info("token respone from keycloak {}", response);

			authToken = (keycloakAuthBean) Utils.convertJsonToObject(response.getData(), keycloakAuthBean.class);
			if (authToken != null) {
				isCorrect = true;
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting access token", e);
		}
		return isCorrect;

	}

	/**
	 * @author ANANDHI VIJAY impersonateKeycloakUser
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public HttpHeaders impersonateKeycloakUser(String userName) throws TclCommonException {
		String userId = getKeycloakUserIdByUserName(userName);
		if (userId != null) {
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			HttpHeaders response = restClientService.postKeyCloakReturnResponseHeaders(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.USERS_URL).concat(userId)
							.concat(Constants.IMPERSONATE_URL),
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
	 * @author ANANDHI VIJAY getKeycloakUserBeanByUserName
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public KeycloakUserResponseBean getKeycloakUserBeanByUserName(String userName) throws TclCommonException {
		try {
			KeycloakUserResponseBean[] userResponse = null;
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(keycloakServerUrl.concat(realmUrl)
					.concat(realm).concat(Constants.USER_URL).concat("?username=").concat(userName), authHeader);
			if (getRoleByNameResponse.getData() != null) {
				userResponse = (KeycloakUserResponseBean[]) Utils.convertJsonToObject(getRoleByNameResponse.getData(),
						KeycloakUserResponseBean[].class);
			} else {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_ROLE, ResponseResource.R_CODE_ERROR);
			}
			if (userResponse != null) {
				List<KeycloakUserResponseBean> keycloakUserResponseBeans = Arrays.asList(userResponse);
				if (keycloakUserResponseBeans != null && !keycloakUserResponseBeans.isEmpty()) {
					return keycloakUserResponseBeans.get(0);
				}
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY constructRequiredActionToAddOTP
	 * @param keycloakUserResponseBean
	 * @return
	 */
	public KeycloakUserResponseBean constructRequiredActionToAddOTP(KeycloakUserResponseBean keycloakUserResponseBean) {
		List<Object> requiredAction = new ArrayList<>();
		requiredAction.add(Constants.CONFIGURE_TOTP);
		keycloakUserResponseBean.setRequiredActions(requiredAction);
		return keycloakUserResponseBean;
	}

	/**
	 * @author ANANDHI VIJAY setRequiredActionToEnableOTP
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean setRequiredActionToEnableOTP(String userName) throws TclCommonException {
		try {
			KeycloakUserResponseBean keycloakUserResponseBean = getKeycloakUserBeanByUserName(userName);
			if (keycloakUserResponseBean == null || keycloakUserResponseBean.getId() == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			keycloakUserResponseBean = constructRequiredActionToAddOTP(keycloakUserResponseBean);
			String request = Utils.convertObjectToJson(keycloakUserResponseBean);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse response = restClientService.putKeyCloak(keycloakServerUrl.concat(realmUrl).concat(realm)
					.concat(Constants.USERS_URL).concat(keycloakUserResponseBean.getId()), request, authHeader);
			if (!response.getStatus().equals(Status.SUCCESS)) {
				throw new TclCommonException(ExceptionConstants.ERROR_CREATE_PD, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	/**
	 * @author ANANDHI VIJAY disableOtpCredetialsForUser
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean disableOtpCredetialsForUser(String userName) throws TclCommonException {
		try {
			String userId = getKeycloakUserIdByUserName(userName);
			if (userId == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			String request = Utils.convertObjectToJson(constructCredentialsToDisable());
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse response = restClientService.putKeyCloak(keycloakServerUrl.concat(realmUrl).concat(realm)
					.concat(Constants.USERS_URL).concat(userId).concat(Constants.DISABLE_CREDENTIALS_TYPE), request,
					authHeader);
			if (!response.getStatus().equals(Status.SUCCESS)) {
				throw new TclCommonException(ExceptionConstants.ERROR_CREATE_PD, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	/**
	 * @author ANANDHI VIJAY constructCredentialsToDisable
	 * @return
	 */
	private List<String> constructCredentialsToDisable() {
		List<String> response = new ArrayList<>();
		response.add("otp");
		return response;
	}

	/**
	 * @author ANANDHI VIJAY disableOtpForTheUser
	 * @param userName
	 * @return
	 * @throws TclCommonException
	 */
	public Boolean disableOtpForTheUser(String userName) throws TclCommonException {
		try {
			KeycloakUserResponseBean keycloakUserResponseBean = getKeycloakUserBeanByUserName(userName);
			if (keycloakUserResponseBean == null) {
				throw new TclCommonException(ExceptionConstants.ERROR_NO_USER, ResponseResource.R_CODE_ERROR);
			}
			removeConfigureOtpFromRequiredCredentials(keycloakUserResponseBean);
			if(keycloakUserResponseBean.getRequiredActions().isEmpty()) {
				keycloakUserResponseBean.setRequiredActions(new ArrayList<>());
			}
			String request = Utils.convertObjectToJson(keycloakUserResponseBean);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse response = restClientService.putKeyCloak(keycloakServerUrl.concat(realmUrl).concat(realm)
					.concat(Constants.USERS_URL).concat(keycloakUserResponseBean.getId()), request, authHeader);
			if (!response.getStatus().equals(Status.SUCCESS)) {
				throw new TclCommonException(ExceptionConstants.ERROR_CREATE_PD, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception ex) {
			throw new TclCommonException(ExceptionConstants.ERROR_GET_ROLE,ex, ResponseResource.R_CODE_ERROR);
		}
		return true;
	}

	/**
	 * @author ANANDHI VIJAY removeConfigureOtpFromRequiredCredentials
	 * @param keycloakUserResponseBean
	 */
	public void removeConfigureOtpFromRequiredCredentials(KeycloakUserResponseBean keycloakUserResponseBean) {
		List<Object> requiredCredentials = new ArrayList<>();
		if (keycloakUserResponseBean.getRequiredActions() != null
				&& !keycloakUserResponseBean.getRequiredActions().isEmpty()) {
			keycloakUserResponseBean.getRequiredActions().stream().forEach(req -> {
				if (!req.toString().equals(Constants.CONFIGURE_TOTP)) {
					requiredCredentials.add(req);
				}
			});
			;
		}
		keycloakUserResponseBean.setRequiredActions(requiredCredentials);
	}

	private static KeycloakIdentityProvider constructIdentityProviderFromEmailId(String emailId) {
		KeycloakIdentityProvider keycloakIdentityProvider = new KeycloakIdentityProvider();
		keycloakIdentityProvider.setIdentityProvider(Constants.SAML);
		keycloakIdentityProvider.setUserId(emailId);
		keycloakIdentityProvider.setUserName(emailId);
		return keycloakIdentityProvider;

	}

	private String createSamlIdentityProviderForTheUser(String userId, String emailId) throws TclCommonException {
		String response = Status.SUCCESS.toString();
		KeycloakIdentityProvider keycloakIdentityProvider = constructIdentityProviderFromEmailId(emailId);
		if (keycloakIdentityProvider != null) {
			String request = Utils.convertObjectToJson(keycloakIdentityProvider);
			keycloakAuthBean accessTokenBean = getAuthToke();
			String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
					+ accessTokenBean.getAccessToken();
			Map<String, String> authHeader = new HashMap<>();
			authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
			RestResponse createRolesResponse = restClientService.postKeyCloak(
					keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.USERS_URL).concat(userId).concat(Constants.IDENTITY_CREATE_URL_SAML), request, authHeader);
			if (!createRolesResponse.getStatus().equals(Status.SUCCESS)) {
				response = null;
			}
		}

		return response;
	}
	
	public Boolean checkWhetherOTPEnabledForTheUser(String userName) throws TclCommonException {
		KeycloakUserResponseBean keycloakUserResponseBean = getKeycloakUserBeanByUserName(userName);
		if(keycloakUserResponseBean!=null) {
			if(keycloakUserResponseBean.getRequiredActions()!=null && !keycloakUserResponseBean.getRequiredActions().isEmpty()) {
				if(keycloakUserResponseBean.getRequiredActions().stream().filter(req->req.toString().equals(Constants.CONFIGURE_TOTP)).findFirst().isPresent()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void clearUserCacheInKeycloak() {
		keycloakAuthBean accessTokenBean = getAuthToke();
		String authAccessToken = KeycloakConstants.BEARER.toString() + CommonConstants.SPACE
				+ accessTokenBean.getAccessToken();
		Map<String, String> authHeader = new HashMap<>();
		authHeader.put(KeycloakConstants.AUTHORIZATION.toString(), authAccessToken);
		RestResponse clearUserCacheResponse = restClientService.postKeyCloak(
				keycloakServerUrl.concat(realmUrl).concat(realm).concat(Constants.CLEAR_USER_CACHE), null, authHeader);
		LOGGER.info("Status code on User cache clearance in keycloak: {}",clearUserCacheResponse.getStatus().getStatusCode());
	}

}
