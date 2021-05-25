
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcProductServiceBean.java class. used to connect
 * with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "create_request_v1" })
public class SfdcProductServiceBean extends BaseBean {

	@JsonProperty("create_request_v1")
	private SfdcProductBean createRequestV1;

	@JsonIgnore
	private Integer quoteLeId;

	@JsonIgnore
	private String productSolutionCode;

	@JsonIgnore
	private Integer parentQuoteToLeId;

	@JsonProperty("create_request_v1")
	public SfdcProductBean getCreateRequestV1() {
		return createRequestV1;
	}

	@JsonProperty("create_request_v1")
	public void setCreateRequestV1(SfdcProductBean createRequestV1) {
		this.createRequestV1 = createRequestV1;
	}

	public String getProductSolutionCode() {
		return productSolutionCode;
	}

	public void setProductSolutionCode(String productSolutionCode) {
		this.productSolutionCode = productSolutionCode;
	}

	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;
	}
}
