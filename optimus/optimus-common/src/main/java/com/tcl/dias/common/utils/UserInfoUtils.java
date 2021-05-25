package com.tcl.dias.common.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.redis.beans.CustomerDetail;
import com.tcl.dias.common.redis.beans.PartnerDetail;
import com.tcl.dias.common.redis.beans.UserInformation;
import com.tcl.dias.common.redis.repo.AuthTokenDetailRepository;
import com.tcl.dias.common.redis.service.AuthTokenService;

/**
 * This file contains the UserInfoUtils.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class UserInfoUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoUtils.class);

	@Autowired
	AuthTokenDetailRepository authTokenDetailRepository;

	@Value("${token.purge.time}")
	private long purgeTime;

	/**
	 * 
	 * getCustomerLinkedToUser-This method returns the additional details related to
	 * the authentication process
	 * 
	 * @return String
	 */
	public List<CustomerDetail> getCustomerDetails() {
		UserInformation userInformation = getUserInformation();
        LOGGER.debug("Customer Details :: {}", userInformation!=null?userInformation.getCustomers():null);
		return userInformation != null ? userInformation.getCustomers() : null;
	}

	/**
	 *
	 * getPartnerLinkedToUser-This method returns the additional details related to
	 * the authentication process
	 *
	 * @return String
	 */
	public List<PartnerDetail> getPartnerDetails() {
		UserInformation userInformation = getUserInformation();
		LOGGER.debug("Partner Details :: {}", userInformation.getPartners());
		return userInformation != null ? userInformation.getPartners() : null;
	}

	/**
	 * getUserInformation
	 */
	public UserInformation getUserInformation() {
		Map<String, Object> userInfo = authTokenDetailRepository.find(Utils.getSource());
		return userInfo.size() != 0 ? (UserInformation) userInfo.get(AuthTokenService.USER_INFORMATION) : null;
	}

	public List<String> getUserRoles() {
		Map<String, Object> userInfo = authTokenDetailRepository.find(Utils.getSource());
		UserInformation userInformation = (UserInformation) userInfo.get(AuthTokenService.USER_INFORMATION);
		return userInformation.getRole();
	}

	public String getUserFullName() {
		UserInformation userInformation = getUserInformation();
		return userInformation != null
				? userInformation.getFirstName()
						+ (userInformation.getLastName() != null ? " " + userInformation.getLastName() : "")
				: null;
	}

	public String getUserType() {
		UserInformation userInformation = getUserInformation();
		return userInformation != null ? userInformation.getUserType() : null;
	}

	public CustomerDetail getCustomerByLeId(Integer customerLeId) {
		for (CustomerDetail customerDetail : getCustomerDetails()) {
			if (customerDetail.getCustomerLeId().equals(customerLeId)) {
				return customerDetail;
			}
		}
		return null;
	}
	
	public Set<Integer> getByErfCustomerId(Integer erfcustomerId) {
		Set<Integer> leIds=new HashSet<>();
		for (CustomerDetail customerDetail : getCustomerDetails()) {
			if (customerDetail.getErfCustomerId().equals(erfcustomerId)) {
				leIds.add(customerDetail.getCustomerLeId());
			}
		}
		return leIds;
	}
	
	public Set<Integer> getByErfPartnerId(Integer erfPartnerId) {
		Set<Integer> leIds=new HashSet<>();
		for (PartnerDetail partnerDetail : getPartnerDetails()) {
			if (partnerDetail.getErfPartnerId().equals(erfPartnerId)) {
				leIds.add(partnerDetail.getPartnerLeId());
			}
		}
		return leIds;
	}

	public PartnerDetail getPartnerByLeId(Integer partnerLeId) {
		return getPartnerDetails().stream().filter(partnerDetail -> partnerDetail.getPartnerLeId()
				.equals(partnerLeId))
				.findFirst().get();
	}

	public static List<String> getRoles(UserInformation userInformation) {
		return userInformation != null ? userInformation.getRole() : null;
	}

	public UserInformation getUserInformation(String userName) {
		Map<String, Object> userInfo = authTokenDetailRepository.find(userName);
		if (userInfo != null && userInfo.size() > 0) {
			return (UserInformation) userInfo.get(AuthTokenService.USER_INFORMATION);
		}
		return null;
	}
	
	public boolean isAction(String actionName) {
		Map<String, Object> userInfo = authTokenDetailRepository.find(Utils.getSource());
		if (userInfo != null && userInfo.size() > 0) {
			UserInformation userInformation = (UserInformation) userInfo.get(AuthTokenService.USER_INFORMATION);
			userInformation.getRole();
			if (userInformation.getRole() != null && userInformation.getRole().contains(actionName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * updateTokenExpiry
	 */
	public void updateAuthExpiry(String userName) {
		authTokenDetailRepository.updateExpiry(userName, purgeTime);
	}
	
	/**
	 * This method is used to get the token details of the logged in user
	 * @author ANANDHI VIJAY
	 * @param request
	 * @return
	 */
	public static String getToken(HttpServletRequest request) {
		String accessToken = null;

		if (null != request) {
			if ((request.getUserPrincipal()) != null) {
				KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) request
						.getUserPrincipal();
				if (keycloakAuthenticationToken != null && keycloakAuthenticationToken.getDetails() != null) {
					SimpleKeycloakAccount simpleKeycloakAccount = (SimpleKeycloakAccount) keycloakAuthenticationToken
							.getDetails();
					if (simpleKeycloakAccount != null && simpleKeycloakAccount.getPrincipal() != null) {
						KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal) simpleKeycloakAccount
								.getPrincipal();
						if (keycloakPrincipal.getKeycloakSecurityContext() != null
								&& keycloakPrincipal.getKeycloakSecurityContext().getTokenString() != null) {
							accessToken = keycloakPrincipal.getKeycloakSecurityContext().getTokenString();
							LOGGER.info("User principal {} ", accessToken);
						}
					}
				}
			}
		}
		return accessToken;
	}

}
