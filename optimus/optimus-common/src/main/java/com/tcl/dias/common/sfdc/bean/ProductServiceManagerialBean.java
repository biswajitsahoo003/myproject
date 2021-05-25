package com.tcl.dias.common.sfdc.bean;

/**
 * This file contains the ProductServiceManagerialBean.java class.
 *used for sfdc
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ProductServiceManagerialBean {

	private ProductServiceBean productService;
	private String opportunityId;
	private String feasibilityCommercialManagerName;
	private String recordTypeName;
	

	/**
	 * @return the productService
	 */
	public ProductServiceBean getProductService() {
		return productService;
	}

	/**
	 * @param productService
	 *            the productService to set
	 */
	public void setProductService(ProductServiceBean productService) {
		this.productService = productService;
	}

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
	 * @return the feasibilityCommercialManagerName
	 */
	public String getFeasibilityCommercialManagerName() {
		return feasibilityCommercialManagerName;
	}

	/**
	 * @param feasibilityCommercialManagerName
	 *            the feasibilityCommercialManagerName to set
	 */
	public void setFeasibilityCommercialManagerName(String feasibilityCommercialManagerName) {
		this.feasibilityCommercialManagerName = feasibilityCommercialManagerName;
	}

	/**
	 * @return the recordTypeName
	 */
	public String getRecordTypeName() {
		return recordTypeName;
	}

	/**
	 * @param recordTypeName
	 *            the recordTypeName to set
	 */
	public void setRecordTypeName(String recordTypeName) {
		this.recordTypeName = recordTypeName;
	}
	
	

}
