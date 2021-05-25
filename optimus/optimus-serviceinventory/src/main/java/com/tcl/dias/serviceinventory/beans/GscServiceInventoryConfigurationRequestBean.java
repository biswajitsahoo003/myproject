package com.tcl.dias.serviceinventory.beans;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Bean class to hold Service Inventory Request Data
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GscServiceInventoryConfigurationRequestBean {

	private Integer customerId;
	private String customerLeId;
	private String secsId;
	private String accessType;
	private String number;
	private List<String> origin;
	private List<String> destination;

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(String customerLeId) {
		this.customerLeId = customerLeId;
	}

	public String getSecsId() {
		return secsId;
	}

	public void setSecsId(String secsId) {
		this.secsId = secsId;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

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
}
