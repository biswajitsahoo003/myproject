package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This class is used as a response bean in owner region
 * 
 *
 * @author Madhumiethaa Palanisamy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OwnerRegionQueryResponseBean {

	private String status;

	private String message;

	private String opportunityOwnersRegionc;

	private String id;

	public String getOpportunityOwnersRegionc() {
		return opportunityOwnersRegionc;
	}

	public void setOpportunityOwnersRegionc(String opportunityOwnersRegionc) {
		this.opportunityOwnersRegionc = opportunityOwnersRegionc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
