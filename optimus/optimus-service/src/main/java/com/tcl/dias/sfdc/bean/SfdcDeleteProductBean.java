
package com.tcl.dias.sfdc.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the SfdcDeleteProductBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "ProductServices", "OpportunityId", "sourceSystem" })
public class SfdcDeleteProductBean extends BaseBean {

	@JsonProperty("ProductServices")
	private List<SfdcDeleteProductServices> productservices;
	@JsonProperty("sourceSystem")
	private String sourceSystem;
	@JsonProperty("OpportunityId")
	private String opportunityId;

	@JsonIgnore
	private String parentTpsSfdcOptyId;

	public List<SfdcDeleteProductServices> getProductservices() {
		return productservices;
	}

	public void setProductservices(List<SfdcDeleteProductServices> productservices) {
		this.productservices = productservices;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getParentTpsSfdcOptyId() {
		return parentTpsSfdcOptyId;
	}

	public void setParentTpsSfdcOptyId(String parentTpsSfdcOptyId) {
		this.parentTpsSfdcOptyId = parentTpsSfdcOptyId;
	}
}
