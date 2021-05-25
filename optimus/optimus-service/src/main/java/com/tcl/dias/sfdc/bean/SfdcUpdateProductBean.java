
package com.tcl.dias.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SfdcProductBean.java class. used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "productsservices", "OpportunityId", "L2FeasibilityCommercialManagerName", "RecordTypeName" })
public class SfdcUpdateProductBean extends BaseBean {

	@JsonProperty("productsservices")
	private SfdcUpdateProductServices productsservices;
	@JsonProperty("OpportunityId")
	private String opportunityId;
	@JsonProperty("L2FeasibilityCommercialManagerName")
	private String l2FeasibilityCommercialManagerName;
	@JsonProperty("RecordTypeName")
	private String recordTypeName;

	public SfdcUpdateProductServices getProductsservices() {
		return productsservices;
	}

	public void setProductsservices(SfdcUpdateProductServices productsservices) {
		this.productsservices = productsservices;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getL2FeasibilityCommercialManagerName() {
		return l2FeasibilityCommercialManagerName;
	}

	public void setL2FeasibilityCommercialManagerName(String l2FeasibilityCommercialManagerName) {
		this.l2FeasibilityCommercialManagerName = l2FeasibilityCommercialManagerName;
	}

	public String getRecordTypeName() {
		return recordTypeName;
	}

	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}

}
