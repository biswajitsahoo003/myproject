package com.tcl.dias.docusign.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This file contains the DocuSignRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DocuSignRequest {

	@JsonProperty("Username")
	private String userName;
	@JsonProperty("Password")
	private String password;
	@JsonProperty("IntegratorKey")
	private String integratorKey;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIntegratorKey() {
		return integratorKey;
	}

	public void setIntegratorKey(String integratorKey) {
		this.integratorKey = integratorKey;
	}

	@Override
	public String toString() {
		return "DocuSignRequest [userName=" + userName + ", password=" + password + ", integratorKey=" + integratorKey
				+ "]";
	}

}
