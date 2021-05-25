package com.tcl.dias.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tcl.dias.auth.constants.Constants;
import com.tcl.dias.auth.constants.ExceptionConstants;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Status;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Used to interact with LDAPUserDao and for user authentication
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class AuthenticationService {

	@Autowired
	KeycloakService keyclockService;

	/**
	 * resetPassword- This method takes userName,
	 * oldPassword,newPassword1,newPassword2 validates new entered passwords and
	 * returns Successful password reseted message
	 * 
	 * @param username
	 * @param newPassword1
	 * @param newPassword2
	 * @return String
	 * @throws TclCommonException
	 */
	public String resetPassword(String username, String newPassword1, String newPassword2) throws TclCommonException {
		String response = Constants.PD_NOT_MATCHED;
		try {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(newPassword1) || StringUtils.isEmpty(newPassword2))

				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			if (newPassword1.equals(newPassword2)) {
				keyclockService.setPasswordForUser(newPassword1, keyclockService.getKeycloakUserIdByUserName(username));
				return Status.SUCCESS.toString();
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	/**
	 * used to return user by using id
	 * 
	 * @param userId
	 * @return User
	 * @throws TclCommonException
	 */

	public String getUserDetailById(String userId) throws TclCommonException {
		try {
			if (StringUtils.isEmpty(userId))

				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

			return keyclockService.getKeycloakUserIdByUserName(userId);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

}
