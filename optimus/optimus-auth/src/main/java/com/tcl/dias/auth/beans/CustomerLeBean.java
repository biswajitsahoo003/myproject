package com.tcl.dias.auth.beans;

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
public class CustomerLeBean {
	
	private Integer customerLeId;
	
	private String customerLeName;

	/**
	 * @return the customerLeId
	 */
	public Integer getCustomerLeId() {
		return customerLeId;
	}

	/**
	 * @param customerLeId the customerLeId to set
	 */
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}

	/**
	 * @return the customerLeName
	 */
	public String getCustomerLeName() {
		return customerLeName;
	}

	/**
	 * @param customerLeName the customerLeName to set
	 */
	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}
	
	
	


}
