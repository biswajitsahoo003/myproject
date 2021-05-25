
package com.tcl.dias.common.sfdc.response.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * SiteResponseBean.class is used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteResponseBean {

	private String opportunityId;
	private String productService;
	private String status;
	private String message;
	private List<SiteLocationResponse> siteLocations = new ArrayList<>();
	private boolean isError;
	private String errorMessage;
	private Boolean isCancel;
	private String prodSolutionCode;

	/**
	 * @return the opportunityId
	 */
	public String getOpportunityId() {
		return opportunityId;
	}

	/**
	 * @param opportunityId the opportunityId to set
	 */
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	/**
	 * @return the productService
	 */
	public String getProductService() {
		return productService;
	}

	/**
	 * @param productService the productService to set
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
	 * @param status the status to set
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
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the siteLocations
	 */
	public List<SiteLocationResponse> getSiteLocations() {
		return siteLocations;
	}

	/**
	 * @param siteLocations the siteLocations to set
	 */
	public void setSiteLocations(List<SiteLocationResponse> siteLocations) {
		this.siteLocations = siteLocations;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public String getProdSolutionCode() {
		return prodSolutionCode;
	}

	public void setProdSolutionCode(String prodSolutionCode) {
		this.prodSolutionCode = prodSolutionCode;
	}

}
