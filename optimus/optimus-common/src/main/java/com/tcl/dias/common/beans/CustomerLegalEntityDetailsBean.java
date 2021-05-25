package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * This file contains the CustomerLegalEntityDetailsBean.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerLegalEntityDetailsBean {

	private List<CustomerLeBean> customerLeDetails;

	/**
	 * @return the customerLeDetails
	 */
	public List<CustomerLeBean> getCustomerLeDetails() {
		return customerLeDetails;
	}

	/**
	 * @param customerLeDetails
	 *            the customerLeDetails to set
	 */
	public void setCustomerLeDetails(List<CustomerLeBean> customerLeDetails) {
		this.customerLeDetails = customerLeDetails;
	}

	@Override
	public String toString() {
		return "CustomerLegalEntityDetailsBean{" +
				"customerLeDetails=" + customerLeDetails +
				'}';
	}
}
