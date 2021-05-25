package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * This file Contains MDM Request Bean information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "corrId",
    "contact"
})
public class MDMRequestBean {
	@JsonProperty("contact")
    private MDMContact Contact;
    @JsonProperty("corrId")
    private String CorrId;
    
    @JsonProperty("contact")
	public MDMContact getContact() {
		return Contact;
	}

    @JsonProperty("contact")
	public void setContact(MDMContact contact) {
		Contact = contact;
	}

	@JsonProperty("corrId")
	public String getCorrId() {
		return CorrId;
	}
    
	@JsonProperty("corrId")
	public void setCorrId(String corrId) {
		CorrId = corrId;
	}

    
	
	
    
    
	}
