package com.tcl.dias.auth.usermgmt.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tcl.dias.auth.usermgmt.beans.AppUserRequest;
import com.tcl.dias.common.utils.Status;

/**
 * This file contains the ApplicationUserService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class ApplicationUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationUserService.class);

	/**
	 * createApplicationUser
	 */
	public String persistApplicationUser(AppUserRequest persistUser) {
		LOGGER.info("Input received for create User {}", persistUser);
		String status = Status.SUCCESS.toString();
		String validateResponse = validateRequest(persistUser);
		if (validateResponse == null) {
			LOGGER.warn("Validation error :: {}", validateResponse);
			return validateResponse;
		}
		// TODO - Call User Management
		return status;
	}
	
	public String updateApplicationUser(AppUserRequest request) {
		LOGGER.info("Input received for update User {}", request);
		String status = Status.SUCCESS.toString();
		String emailId = request.getEmailId();
		if (emailId == null) {
			LOGGER.warn("Validation error :: {}", emailId);
			return "username/emailId is manadatory";
		}
		// TODO - Call User Management
		return status;
	}

	public String deleteApplicationUser(AppUserRequest request) {
		LOGGER.info("Input received for deleting User {}", request);
		String status = Status.SUCCESS.toString();
		String emailId = request.getEmailId();
		if (emailId == null) {
			LOGGER.warn("Validation error :: {}", emailId);
			return "username/emailId is manadatory";
		}
		// TODO - Call User Management and persist
		return status;
	}

	/**
	 * validateRequest
	 * 
	 * @param firstName
	 * @param lastName
	 * @param emailId
	 * @param contactNo
	 */
	private String validateRequest(AppUserRequest persistUser) {
		if (StringUtils.isNotBlank(persistUser.getContactNo()) && StringUtils.isNotBlank(persistUser.getEmailId())
				&& StringUtils.isNotBlank(persistUser.getFirstName())
				&& StringUtils.isNotBlank(persistUser.getLastName())) {
			return "All the fields are manadatory";
		}
		return null;
	}

}
