package com.tcl.dias.auth.beans;

import javax.validation.constraints.Pattern;

import com.tcl.dias.auth.constants.BeanValidationConstants;

/**
 * Used for carrying the request parameters to reset password request
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ResetPasswordRequest {

	@Pattern(regexp = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})", message = BeanValidationConstants.INVALID_PD)

	private String password;

	/**
	 * returns a re-set password to user as String
	 * 
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * used to set given password to current class filed
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "ResetPasswordRequest [password=" + password + "]";
	}

}
