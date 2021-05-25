package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to customer le and configuration info
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SIConfigurationByLeBean {

	private String customerLeName;

	private Integer customerLeId;

	private String accessType;

	private String numbersCount;

	private String siteCount;

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getNumbersCount() {
		return numbersCount;
	}

	public void setNumbersCount(String numbersCount) {
		this.numbersCount = numbersCount;
	}

	public String getSiteCount() {
		return siteCount;
	}

	public void setSiteCount(String siteCount) {
		this.siteCount = siteCount;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}
}
