package com.tcl.dias.serviceinventory.beans;

import java.util.List;
import java.util.Set;

/**
 * Bean class to hold Service Inventory Data Bean details
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIConfigurationCountryBean {

	private List<String> origin;

	private List<String> destination;

	private String accessType;

	private Integer numbersCount;

	private String customerLeName;

	private Integer customerLeId;

	private Integer siteCount;

	public List<String> getOrigin() {
		return origin;
	}

	public void setOrigin(List<String> origin) {
		this.origin = origin;
	}

	public List<String> getDestination() {
		return destination;
	}

	public void setDestination(List<String> destination) {
		this.destination = destination;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public Integer getNumbersCount() {
		return numbersCount;
	}

	public void setNumbersCount(Integer numbersCount) {
		this.numbersCount = numbersCount;
	}

	public String getCustomerLeName() {
		return customerLeName;
	}

	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	public Integer getSiteCount() {
		return siteCount;
	}

	public void setSiteCount(Integer siteCount) {
		this.siteCount = siteCount;
	}
}
