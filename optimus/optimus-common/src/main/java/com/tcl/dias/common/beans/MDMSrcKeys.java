package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file Contains MDM SRC Key information
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "srcKeyID",
    "srcKeyIDType",
    })

public class MDMSrcKeys {
	@JsonProperty("srcKeyID")
	private String srcKeyID;
	@JsonProperty("srcKeyIDType")
	private String srcKeyIDType;
	
	@JsonProperty("srcKeyID")
	public String getSrcKeyID() {
		return srcKeyID;
	}
	@JsonProperty("srcKeyID")
	public void setSrcKeyID(String srcKeyID) {
		this.srcKeyID = srcKeyID;
	}
	
	@JsonProperty("srcKeyIDType")
	public String getSrcKeyIDType() {
		return srcKeyIDType;
	}
	@JsonProperty("srcKeyIDType")
	public void setSrcKeyIDType(String srcKeyIDType) {
		this.srcKeyIDType = srcKeyIDType;
	}
	

}
