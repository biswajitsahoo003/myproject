package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file Contains MDM contact method   information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "contactValue",
    "contactMethodStartDate",
    "contactMethodUsage",
    "contactMethod"
    
})

public class MDMContactMethod {
	
	@JsonProperty("contactValue")
	private String contactValue;
	@JsonProperty("contactMethodStartDate")
	private String contactMethodStartDate;
	@JsonProperty("contactMethodUsage")
	private String contactMethodUsage;
	@JsonProperty("contactMethod")
	private String contactMethod;
	
	@JsonProperty("contactValue")
	public String getContactValue() {
		return contactValue;
	}
	@JsonProperty("contactValue")
	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}
	
	@JsonProperty("contactMethodStartDate")
	public String getContactMethodStartDate() {
		return contactMethodStartDate;
	}
	@JsonProperty("contactMethodStartDate")
	public void setContactMethodStartDate(String contactMethodStartDate) {
		this.contactMethodStartDate = contactMethodStartDate;
	}
	
	@JsonProperty("contactMethodUsage")
	public String getContactMethodUsage() {
		return contactMethodUsage;
	}
	@JsonProperty("contactMethodUsage")
	public void setContactMethodUsage(String contactMethodUsage) {
		this.contactMethodUsage = contactMethodUsage;
	}
	
	@JsonProperty("contactMethod")
	public String getContactMethod() {
		return contactMethod;
	}
	@JsonProperty("contactMethod")
	public void setContactMethod(String contactMethod) {
		this.contactMethod = contactMethod;
	}
	
	


}
