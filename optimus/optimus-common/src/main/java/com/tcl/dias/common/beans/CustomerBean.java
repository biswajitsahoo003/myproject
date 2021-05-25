package com.tcl.dias.common.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * This file contains the CustomerBean.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class CustomerBean {

	private Set<CustomerDetailBean> customerDetailsSet = new HashSet<>();

	/**
	 * @return the customerDetailsSet
	 */
	public Set<CustomerDetailBean> getCustomerDetailsSet() {
		return customerDetailsSet;
	}

	/**
	 * @param customerDetailsSet
	 *            the customerDetailsSet to set
	 */
	public void setCustomerDetailsSet(Set<CustomerDetailBean> customerDetailsSet) {
		this.customerDetailsSet = customerDetailsSet;
	}

}
