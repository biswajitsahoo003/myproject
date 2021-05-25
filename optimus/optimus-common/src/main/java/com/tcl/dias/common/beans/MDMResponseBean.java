package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file Contains MDM Response Bean  information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "status",
    "message",
    "corrId",
    "partyId",
    "firstName",
    "lastName",
    "contactId",
    "srcKeys"
})
public class MDMResponseBean {
	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("corrId")
	private String corrId;
	@JsonProperty("partyId")
	private String partyId;
	@JsonProperty("firstName")
	private String firstName;
	@JsonProperty("lastName")
	private String lastName;
	@JsonProperty("contactId")
	private String contactId;
	@JsonProperty("srcKey")
	private MDMSrcKeys srcKeys;  
	
	
	@JsonProperty("contactId")
	public String getContactId() {
		return contactId;
	}
	@JsonProperty("contactId")
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}
	
	@JsonProperty("corrId")
	public String getCorrId() {
		return corrId;
	}
	@JsonProperty("corrId")
	public void setCorrId(String corrId) {
		this.corrId = corrId;
	}

	@JsonProperty("partyId")
	public String getPartyId() {
		return partyId;
	}
	@JsonProperty("partyId")
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	
	@JsonProperty("firstName")
	public String getFirstName() {
		return firstName;
	}
	@JsonProperty("firstName")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("lastName")
	public String getLastName() {
		return lastName;
	}
	@JsonProperty("lastName")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("srcKeys")
	public MDMSrcKeys getSrcKeys() {
		return srcKeys;
	}
	@JsonProperty("srcKeys")
	public void setSrcKeys(MDMSrcKeys srcKeys) {
		this.srcKeys = srcKeys;
	}
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}
	

}
