package com.tcl.dias.common.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.Utils;

/**
 * This Class is used for Custom Roles
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CustomKeycloakAuthenticationProvider extends KeycloakAuthenticationProvider {

	public CustomKeycloakAuthenticationProvider(ApplicationContext appCtx) {
		this.appCtx = appCtx;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomKeycloakAuthenticationProvider.class);

	private ApplicationContext appCtx;

	/**
	 * 
	 * authenticate - Customer Authentication
	 * 
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserInformation userInformations = null;
		UserInfoUtils userInfoUtils = appCtx.getBean(UserInfoUtils.class);
		String userName = Utils.getSource();
		KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
		//Collection<? extends GrantedAuthority> keycloakAuthorities = mapAuthorities(addKeycloakRoles(token));
		MQUtils mqUtils = appCtx.getBean(MQUtils.class);
		if (userInfoUtils.getUserInformation(userName) == null) {
			try {
				LOGGER.info("MDC Filter token value in before Queue call authenticate {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Map<String, String> userInfoMapper = new HashMap<>();
				String accessToken =getToken(authentication.getPrincipal());
				userInfoMapper.put("username", userName);
				userInfoMapper.put("token", accessToken);
				String req = Utils.convertObjectToJson(userInfoMapper);
				LOGGER.info("The request passed {}", req);
				mqUtils.sendAndReceive("queue.user.info", req);
			} catch (Exception e) {
				LOGGER.info("Error in saving the roles", e);
			}
		}
		userInformations = userInfoUtils.getUserInformation(userName);
		Collection<? extends GrantedAuthority> grantedAuthorities = addUserSpecificAuthorities(authentication,
				null, userInformations);
		return new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), grantedAuthorities);
	}

	/**
	 * mapAuthorityMapper - Mapping the prefix
	 * 
	 * @return
	 */
	protected SimpleAuthorityMapper mapAuthorityMapper() {
		SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
		grantedAuthorityMapper.setPrefix("ROLE_");
		grantedAuthorityMapper.setConvertToUpperCase(true);
		return grantedAuthorityMapper;
	}

	/**
	 * Adding custom Roles either from Redis addUserSpecificAuthorities
	 * 
	 * @param authentication
	 * @param authorities
	 * @return
	 */
	protected Collection<? extends GrantedAuthority> addUserSpecificAuthorities(Authentication authentication,
			Collection<? extends GrantedAuthority> authorities, UserInformation userInformations) {
		List<GrantedAuthority> result = new ArrayList<>();
		if (authorities != null)
			result.addAll(authorities);
		// TODO -> To Get from User Management
		if (userInformations != null && userInformations.getRole() != null) {
			for (String role : userInformations.getRole()) {
				LOGGER.info("Role Received {}", role);
				if (role != null)
					result.add(new SimpleGrantedAuthority(role));
			}
		} else {
			result.add(new SimpleGrantedAuthority("OPTIMUS_USER"));
		}
		// return mapAuthorityMapper().mapAuthorities(result);
		return result;
	}

	/**
	 * 
	 * addKeycloakRoles - Mapping the roles from the Keycloak
	 * 
	 * @param token
	 * @return
	 */
	protected Collection<? extends GrantedAuthority> addKeycloakRoles(KeycloakAuthenticationToken token) {
		Collection<GrantedAuthority> keycloakRoles = new ArrayList<>();
		for (String role : token.getAccount().getRoles()) {
			keycloakRoles.add(new SimpleGrantedAuthority(role));
		}
		return keycloakRoles;
	}

	private Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		return authorities;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
	}

	@SuppressWarnings("unchecked")
	public String getToken(Object userPrincipal) {
		String accessToken = null;
		if (null != userPrincipal) {
			if (userPrincipal instanceof KeycloakPrincipal) {
				LOGGER.info("Inside Keycloak Security Contect pricipal");
				KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) userPrincipal;
				accessToken = kp.getKeycloakSecurityContext().getTokenString();
				LOGGER.info("Access Token Generated :::  {} ", accessToken);
			}
		}
		return accessToken;
	}

}
