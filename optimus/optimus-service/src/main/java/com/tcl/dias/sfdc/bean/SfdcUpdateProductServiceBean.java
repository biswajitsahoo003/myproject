
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
@JsonPropertyOrder({ "update_request_v1" })
public class SfdcUpdateProductServiceBean extends BaseBean {

	@JsonProperty("update_request_v1")
	private SfdcUpdateProductBean updateRequestV1;

	@JsonIgnore
	private String productSolutionCode;

	@JsonIgnore
	private Boolean isCancel;

	@JsonIgnore
	private Integer parentQuoteToLeId;

	public SfdcUpdateProductBean getUpdateRequestV1() {
		return updateRequestV1;
	}

	public void setUpdateRequestV1(SfdcUpdateProductBean updateRequestV1) {
		this.updateRequestV1 = updateRequestV1;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public String getProductSolutionCode() {
		return productSolutionCode;
	}

	public void setProductSolutionCode(String productSolutionCode) {
		this.productSolutionCode = productSolutionCode;
	}

	public Integer getParentQuoteToLeId() {
		return parentQuoteToLeId;
	}

	public void setParentQuoteToLeId(Integer parentQuoteToLeId) {
		this.parentQuoteToLeId = parentQuoteToLeId;
	}
}
