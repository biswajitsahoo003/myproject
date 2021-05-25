
package com.tcl.dias.sfdc.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SiteSolutionBean.java class.
 * used to connect with sdfc
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "OpportunityID", "ProductServiceID", "SourceSystem", "SourceSytemTransactionID", "SiteLocations" })
public class SfdcSiteSolutionBean  extends BaseBean{

	@JsonProperty("OpportunityID")
	private String opportunityID;
	@JsonProperty("ProductServiceID")
	private String productServiceID;
	@JsonProperty("SourceSystem")
	private String sourceSystem;
	@JsonProperty("SourceSytemTransactionID")
	private String sourceSytemTransactionID;
	@JsonProperty("SiteLocations")
	private List<SfdcSiteLocation> siteLocations = null;
	@JsonIgnore
	private Boolean isCancel;
	@JsonIgnore
	private String productSolutionCode;

	@JsonProperty("OpportunityID")
	public String getOpportunityID() {
		return opportunityID;
	}

	@JsonProperty("OpportunityID")
	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}

	@JsonProperty("ProductServiceID")
	public String getProductServiceID() {
		return productServiceID;
	}

	@JsonProperty("ProductServiceID")
	public void setProductServiceID(String productServiceID) {
		this.productServiceID = productServiceID;
	}

	@JsonProperty("SourceSystem")
	public String getSourceSystem() {
		return sourceSystem;
	}

	@JsonProperty("SourceSystem")
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	@JsonProperty("SourceSytemTransactionID")
	public String getSourceSytemTransactionID() {
		return sourceSytemTransactionID;
	}

	@JsonProperty("SourceSytemTransactionID")
	public void setSourceSytemTransactionID(String sourceSytemTransactionID) {
		this.sourceSytemTransactionID = sourceSytemTransactionID;
	}

	@JsonProperty("SiteLocations")
	public List<SfdcSiteLocation> getSiteLocations() {
		if (siteLocations == null) {
			siteLocations = new ArrayList<>();
		}
		return siteLocations;
	}

	@JsonProperty("SiteLocations")
	public void setSiteLocations(List<SfdcSiteLocation> siteLocations) {
		this.siteLocations = siteLocations;
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
	
	

}
