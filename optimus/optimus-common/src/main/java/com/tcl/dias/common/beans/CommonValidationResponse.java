package com.tcl.dias.common.beans;

/**
 * This file contains the CommonValidationResponse.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CommonValidationResponse {

	private boolean status;
	private String validationMessage;

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	@Override
	public String toString() {
		return "CommonValidationResponse [status=" + status + ", validationMessage=" + validationMessage + "]";
	}

}
