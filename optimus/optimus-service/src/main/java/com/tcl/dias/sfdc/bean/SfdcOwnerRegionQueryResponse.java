package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.sfdc.response.bean.SfdcAttributes;

/**
 * This file contains the SfdcOwnerRegionQueryResponse.java class.
 * 
 * @author Madhumiethaa Palansiamy
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "attributes", "Opportunity_Owner_s_Region__c", "Id" })
public class SfdcOwnerRegionQueryResponse {

	@JsonProperty("attributes")
	private SfdcAttributes attributes;

	@JsonProperty("Opportunity_Owner_s_Region__c")
	private String opportunityOwnersRegionc;

	public SfdcAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(SfdcAttributes attributes) {
		this.attributes = attributes;
	}

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

	@JsonProperty("Id")
	private String id;

}
