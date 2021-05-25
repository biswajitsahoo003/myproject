
package com.tcl.dias.sfdc.response.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the SIteResponseBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "OpportunityID", "ProductService", "status", "message", "SiteDetails" })
public class SiteResponseBean {

	@JsonProperty("OpportunityID")
	private String opportunityID;
	@JsonProperty("ProductService")
	private String productService;
	@JsonProperty("status")
	private String status;
	@JsonProperty("message")
	private String message;
	@JsonProperty("SiteDetails")
	private List<SfdcSiteResponse> siteLocations = null;

	/**
	 * @return the opportunityID
	 */
	public String getOpportunityID() {
		return opportunityID;
	}

	/**
	 * @param opportunityID
	 *            the opportunityID to set
	 */
	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}

	/**
	 * @return the productService
	 */
	public String getProductService() {
		return productService;
	}

	/**
	 * @param productService
	 *            the productService to set
	 */
	public void setProductService(String productService) {
		this.productService = productService;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the siteLocations
	 */
	public List<SfdcSiteResponse> getSiteLocations() {
		return siteLocations;
	}

	/**
	 * @param siteLocations
	 *            the siteLocations to set
	 */
	public void setSiteLocations(List<SfdcSiteResponse> siteLocations) {
		this.siteLocations = siteLocations;
	}

}
