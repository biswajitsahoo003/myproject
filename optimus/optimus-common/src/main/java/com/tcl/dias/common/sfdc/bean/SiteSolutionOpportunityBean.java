package com.tcl.dias.common.sfdc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the SiteSolutionOpportunityBean.java class. used for sfdc
 * 
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SiteSolutionOpportunityBean {

	private String opportunityId;
	private String productServiceId;
	private String sourceSystem;
	private String sourceSytemTransactionId;
	private String productSolutionCode;
	private Boolean isCancel;
	private List<SiteOpportunityLocation> siteOpportunityLocations;
	
	//Introduced for IZO SDWAN
	private String siteProduct;

	/**
	 * @return the opportunityId
	 */
	public String getOpportunityId() {
		return opportunityId;
	}

	/**
	 * @param opportunityId
	 *            the opportunityId to set
	 */
	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	/**
	 * @return the productServiceId
	 */
	public String getProductServiceId() {
		return productServiceId;
	}

	/**
	 * @param productServiceId
	 *            the productServiceId to set
	 */
	public void setProductServiceId(String productServiceId) {
		this.productServiceId = productServiceId;
	}

	/**
	 * @return the sourceSystem
	 */
	public String getSourceSystem() {
		return sourceSystem;
	}

	/**
	 * @param sourceSystem
	 *            the sourceSystem to set
	 */
	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	/**
	 * @return the sourceSytemTransactionId
	 */
	public String getSourceSytemTransactionId() {
		return sourceSytemTransactionId;
	}

	/**
	 * @param sourceSytemTransactionId
	 *            the sourceSytemTransactionId to set
	 */
	public void setSourceSytemTransactionId(String sourceSytemTransactionId) {
		this.sourceSytemTransactionId = sourceSytemTransactionId;
	}

	/**
	 * @return the siteOpportunityLocations
	 */
	public List<SiteOpportunityLocation> getSiteOpportunityLocations() {
		return siteOpportunityLocations;
	}

	/**
	 * @param siteOpportunityLocations
	 *            the siteOpportunityLocations to set
	 */
	public void setSiteOpportunityLocations(List<SiteOpportunityLocation> siteOpportunityLocations) {

		if (siteOpportunityLocations == null) {
			siteOpportunityLocations = new ArrayList<>();
		}
		this.siteOpportunityLocations = siteOpportunityLocations;
	}

	public String getProductSolutionCode() {
		return productSolutionCode;
	}

	public void setProductSolutionCode(String productSolutionCode) {
		this.productSolutionCode = productSolutionCode;
	}

	public Boolean getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Boolean isCancel) {
		this.isCancel = isCancel;
	}

	public String getSiteProduct() {
		return siteProduct;
	}

	public void setSiteProduct(String siteProduct) {
		this.siteProduct = siteProduct;
	}
	
	

}
