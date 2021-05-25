package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcOpportunityBundleRequest.java class. used to
 * connect with sdfc
 * 
 * @author vpachava
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "create_request_v1" })
public class SfdcOpportunityBundleRequest  extends BaseBean{

	@JsonProperty("create_request_v1")
	private SfdcManagerialBundleOpportunityBean createRequestV1;

	public SfdcManagerialBundleOpportunityBean getCreateRequestV1() {
		return createRequestV1;
	}

	public void setCreateRequestV1(SfdcManagerialBundleOpportunityBean createRequestV1) {
		this.createRequestV1 = createRequestV1;
	}

}
