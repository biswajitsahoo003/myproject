package com.tcl.dias.auth.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Bean class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerBean {

	private Integer customerId;
	
	private String customerName;
	
	private List<CustomerLeBean> customerLeList;

	/**
	 * @return the customerId
	 */
	public Integer getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the customerLeList
	 */
	public List<CustomerLeBean> getCustomerLeList() {
		return customerLeList;
	}

	/**
	 * @param customerLeList the customerLeList to set
	 */
	public void setCustomerLeList(List<CustomerLeBean> customerLeList) {
		this.customerLeList = customerLeList;
	}
	
	
	
	
	
	
	
}
