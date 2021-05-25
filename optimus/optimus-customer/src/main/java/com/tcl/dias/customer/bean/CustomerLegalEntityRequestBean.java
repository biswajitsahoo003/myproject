package com.tcl.dias.customer.bean;

import java.util.List;

/**
 * This file contains the CustomerLegalEntityRequestBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLegalEntityRequestBean {
	
	
	private Integer customerLegalEntityId;
	
	private String productName;
	
	private List<SiteCountryBean> siteCountry;

	/**
	 * @return the customerLegalEntityId
	 */
	public Integer getCustomerLegalEntityId() {
		return customerLegalEntityId;
	}

	/**
	 * @param customerLegalEntityId the customerLegalEntityId to set
	 */
	public void setCustomerLegalEntityId(Integer customerLegalEntityId) {
		this.customerLegalEntityId = customerLegalEntityId;
	}

	/**
	 * @return the siteCountry
	 */
	public List<SiteCountryBean> getSiteCountry() {
		
		return siteCountry;
	}

	/**
	 * @param siteCountry the siteCountry to set
	 */
	public void setSiteCountry(List<SiteCountryBean> siteCountry) {
		this.siteCountry = siteCountry;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	
	
	

}
