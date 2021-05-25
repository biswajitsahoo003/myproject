package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the CustomerLeLocationBean.java class.
 * 
 * this is used for data transfer between oms and location Microservice
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLeLocationBean {

	private Integer erfCustomerLeId;

	private Integer customerId;

	private List<Integer> locationIds;

	/**
	 * @return the erfCustomerLeId
	 */
	public Integer getErfCustomerLeId() {
		return erfCustomerLeId;
	}

	/**
	 * @param erfCustomerLeId
	 *            the erfCustomerLeId to set
	 */
	public void setErfCustomerLeId(Integer erfCustomerLeId) {
		this.erfCustomerLeId = erfCustomerLeId;
	}

	/**
	 * @return the locationIds
	 */
	public List<Integer> getLocationIds() {
		if (locationIds == null) {
			locationIds = new ArrayList<>();
		}
		return locationIds;
	}

	/**
	 * @param locationIds
	 *            the locationIds to set
	 */
	public void setLocationIds(List<Integer> locationIds) {
		this.locationIds = locationIds;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
