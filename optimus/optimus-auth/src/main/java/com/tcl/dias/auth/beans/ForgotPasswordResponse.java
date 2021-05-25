package com.tcl.dias.auth.beans;

/**
 * Used for carrying the response parameters for Forgot password response
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ForgotPasswordResponse {

	/**
	 * parameterized constructor with String
	 * 
	 * @param message
	 */
	public ForgotPasswordResponse(String message) {
		this.message = message;
	}

	public ForgotPasswordResponse() {
	}

	private String message;

	/*
	 * returns a needful message to user as String
	 * 
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/*
	 * used to set response message to current class filed
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ForgotPasswordResponse [message=" + message + "]";
	}

}
