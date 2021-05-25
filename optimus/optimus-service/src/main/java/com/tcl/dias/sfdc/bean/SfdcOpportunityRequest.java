
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcOpportunityRequest.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "create_request_v1" })
public class SfdcOpportunityRequest extends BaseBean {

	@JsonProperty("create_request_v1")
	private SfdcManagerialOpportunityBean createRequestV1;

	@JsonIgnore
	private Integer quoteToLeId;

	@JsonProperty("create_request_v1")
	public SfdcManagerialOpportunityBean getCreateRequestV1() {
		return createRequestV1;
	}

	@JsonProperty("create_request_v1")
	public void setCreateRequestV1(SfdcManagerialOpportunityBean createRequestV1) {
		this.createRequestV1 = createRequestV1;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}
}
