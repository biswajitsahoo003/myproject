package com.tcl.dias.auth.beans;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.tcl.dias.auth.constants.BeanValidationConstants;

/**
 * Used for carrying the request parameters for Forgot password request
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ForgotPasswordRequest {

	@NotNull(message = BeanValidationConstants.EMAIL_ADDRESS_IS_COMPULSORY)
	@Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\." + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
			+ "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = BeanValidationConstants.EMAIL_ADDRESS_IS_NOT_A_VALID_FORMAT)
	private String emailId;

	/*
	 * returns emailId of user as String
	 * 
	 * @return String
	 */
	public String getEmailId() {
		return emailId;
	}

	/*
	 * used to set user mail id to current class filed
	 * 
	 * @param emailId
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	@Override
	public String toString() {
		return "ForgotPasswordRequest [emailId=" + emailId + "]";
	}

}
